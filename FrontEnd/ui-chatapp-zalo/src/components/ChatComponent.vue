<template>
	<div>
		<vue-advanced-chat height="calc(100vh - 20px)" :current-user-id="currentUserId" :rooms="JSON.stringify(rooms)"
			:loading-rooms="loadingRooms" @fetch-more-rooms="fetchMoreRooms" :rooms-loaded="roomsLoaded"
			:room-actions="JSON.stringify(roomActions)" :menu-actions="JSON.stringify(menuActions)"
			:messages="JSON.stringify(messages)" :messages-loaded="messagesLoaded" :load-first-room="loadFirstRoom"
			:show-footer="!hasSystemMessage" @send-message="sendMessage($event.detail[0])" :room-info-enabled="roomInfo"
			@fetch-messages="fetchMessages($event.detail[0])" :templates-text="JSON.stringify(templatesText)"
			@delete-message="deleteMessage($event.detail[0])" @send-message-reaction="sendMessageReaction($event.detail[0])"
			:theme="theme" @room-info="showRoomInfo($event.detail[0])" @edit-message="editMessage($event.detail[0])"
			@room-action-handler="roomActionHandler($event.detail[0])"
			@menu-action-handler="menuActionHandler($event.detail[0])" :emoji-data-source="emojiDataSource" />
		<v-dialog class="dialog-container-user" v-model="showPopUpInfoRoomWith2Members" max-width="352px"
			@click:outside="closePopupInfoRoom">
			<v-card class="dialog-component-user">
				<v-card-title class="dialog-title-user">
					<h2 class="title-user">Thông tin tài khoản
						<div class="icon-close-user" @click="closePopupInfoRoom"><font-awesome-icon icon="fa-solid fa-x" />
						</div>
					</h2>
				</v-card-title>
				<hr style="border: none; border-bottom: 1px solid #ccc;">
				<v-card-text class="dialog-content-user">
					<div class="profile-photo-user">
						<div class="cover-avatar-user">
							<img class="cover-image-user" :src="userFound.imageCoverAvatar" alt="None"
								crossorigin="anonymous">
						</div>
						<div class="ava-name-container-user">
							<div class="avatar-profile-user">
								<div class="avatar-user">
									<img class="avatar-image-user" :src="userFound.imageAvatar">
								</div>
							</div>
							<div class="fullname-profile-user">
								<div class="fullname-user">{{ userFound.userName }}</div>
							</div>
						</div>
					</div>
					<hr style="border: none; border-bottom: 1px solid #ccc;">
					<div class="profile-information-user">
						<div class="profile-header-user">
							<strong>Thông tin cá nhân</strong>
						</div>
						<div>
							<div class="user-profile-details-user">
								<div class="user-profile-item-user">
									<span class="title-user">Điện thoại</span>
									<span class="content-user">{{ userFound.phoneNumber }}</span>
								</div>
								<div class="user-profile-item-user">
									<span class="title-user">Giới tính</span>
									<span class="content-user">{{ userFound.gender === 'Male' ? 'Nam' : 'Nữ' }}</span>
								</div>
								<div class="user-profile-item-user">
									<span class="title-user">Ngày sinh</span>
									<span class="content-user">{{ displayedDate }}</span>
								</div>
							</div>
						</div>
					</div>
					<div class="mb-2"></div>
				</v-card-text>
			</v-card>
		</v-dialog>
		<v-dialog class="dialog-container-user" v-model="showPopUpInfoRoomWithMembers" max-width="352px"
			@click:outside="closePopupInfoRoom">
			<v-card class="dialog-component-user">
				<v-card-title class="dialog-title-user">
					<h2 class="title-user">Thông tin nhóm chat
						<div class="icon-close-user" @click="closePopupInfoRoom"><font-awesome-icon icon="fa-solid fa-x" />
						</div>
					</h2>
				</v-card-title>
				<hr style="border: none; border-bottom: 1px solid #ccc;">
				<v-card-text class="dialog-content-user">
					<div class="profile-photo-user">
						<div class="cover-avatar-user">
							<img class="cover-image-user" :src="roomDetail.roomAvatar" alt="None" crossorigin="anonymous">
						</div>
						<div class="ava-name-container-user">
							<div class="avatar-profile-user">
								<div class="avatar-user">
									<img class="avatar-image-user" :src="roomDetail.roomAvatar">
								</div>
							</div>
							<div class="fullname-profile-user">
								<div class="fullname-user">{{ roomDetail.roomName }}</div>
							</div>
						</div>
					</div>
					<hr style="border: none; border-bottom: 1px solid #ccc;">
					<div class="profile-information-user">
						<div class="profile-header-user">
							<strong>Thành viên</strong>
						</div>
						<div>
							<div class="user-profile-details-user">
								<div class="user-profile-item-user">
									<span class="title-room">Số lượng thành viên</span>
									<span class="content-room">{{ roomDetail.membersCount }}</span>
								</div>
								<div class="user-profile-item-user">
									<div class="mt-4"><span>Danh sách thành viên</span></div>
								</div>
								<div class="user-profile-item-user">
									<div class="friend-list-container">
										<div v-for="member in roomDetail.users" v-bind:key="member._id"
											class="position-relative">
											<div class="friend-info friend-container">
												<div class="avatar-container" @click="showUserInfoDialog(member)">
													<div class="avatar-wrapper">
														<img :src="member.avatar" class="avatar">
													</div>
												</div>
												<div class="detail" @click="showUserInfoDialog(member)">
													<span>{{ member.username }}</span>
												</div>
											</div>
										</div>
									</div>
								</div>

							</div>
						</div>
					</div>
					<div class="mb-2"></div>
				</v-card-text>
			</v-card>
		</v-dialog>
		<v-dialog class="dialog-container" v-model="showPopUpInviteUserToRoom" max-width="500px"
			@click:outside="closeChooseFriendDialog">
			<v-card class="dialog-component">
				<v-card-title class="dialog-title">
					<h2 class="title">Mời bạn bè vào nhóm
						<div class="icon-close" @click="closeChooseFriendDialog"><font-awesome-icon icon="fa-solid fa-x" />
						</div>
					</h2>
				</v-card-title>
				<hr style="border: none; border-bottom: 1px solid #ccc;">
				<v-card-text class="dialog-content">
					<div class="pt-4 pl-4 pr-4">
						<input type="text" v-model="searchText" placeholder="Tìm kiếm theo tên" class="search-input" />
						<div v-if="addedFriends.length !== 0"><span>Đã chọn để thêm vào nhóm</span></div>
						<div class="update-file-container" style="height:100px" v-if="addedFriends.length !== 0">
							<div v-for="friend in addedFriends" v-bind:key="friend.phoneNumber" class="position-relative">
								<div class="friend-info cursor-pointer m-2" @click="deleteFriendTag(friend)">
									<div :class="{ 'wrap': shouldWrap }" class="detail" style="border: 1px solid #ccc;
                                                           border-radius: 8px;
                                                           display: flex;
                                                           justify-content: space-between;
                                                           align-items: center;
                                                           padding: 8px;">
										<span class="color-blue">{{ friend.userName }}
											<font-awesome-icon icon="fa-solid fa-x" /></span>
									</div>
								</div>
							</div>
						</div>
						<div><span>Danh sách bạn bè</span></div>
						<div class="friend-list-container">
							<div v-for="friend in filteredFriends" v-bind:key="friend.phoneNumber"
								class="position-relative">
								<div class="friend-info friend-container" @click="addFriendTag(friend)">
									<div class="avatar-container">
										<div class="avatar-wrapper">
											<img :src="friend.imageAvatar" class="avatar object-cover object-bottom">
										</div>
									</div>
									<div class="detail">
										<span>{{ friend.userName }}</span>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="mt-4"></div>
					<div class="profile-action" @click="addUsersToGroup">
						<div class="items-center text-center cursor-pointer bg-blue-400 rounded-lg h-10 mx-4 px-4 py-2">
							Thêm vào nhóm
						</div>
					</div>
				</v-card-text>
			</v-card>
		</v-dialog>

	</div>
</template>

<script>
import { register } from 'vue-advanced-chat'
import axios from "axios";
import { format, parseISO } from 'date-fns';
import { useToast } from "vue-toastification";
import viLocale from 'date-fns/locale/vi';
// import UserInfo from './UserInfo.vue';
import SockJS from 'sockjs-client';
import Stomp from "webstomp-client";
// import { register } from '../../vue-advanced-chat/dist/vue-advanced-chat.es.js'
register()

export default {
	components: {
		// UserInfo
	},
	computed: {
		filteredFriends() {
			const normalizedSearchText = this.searchText.toLowerCase();
			return this.listFriends.filter(friend =>
				friend.userName.toLowerCase().includes(normalizedSearchText)
			);
		},
		hasSystemMessage() {
			return this.messages.some(message => message.system);
		}
	},
	setup() {
		// Get toast interface
		const toast = useToast(

		);
		return { toast }
	},
	data() {
		return {
			peerConnection: null,
			dataChannel: null,
			socket: null,
			stompClient: null,
			currentUserId: null,
			userFound: null,
			listFriends: [],
			addedFriends: [],
			searchText: "",
			theme: 'light',
			currentRoom: null,
			currentRoomInfo: null,
			RTCConnectionList: [],
			rooms: [],
			roomDetail: null,
			roomsLoaded: false,
			loadingRooms: true,
			loadFirstRoom: true,
			roomInfo: true,
			roomActions: [
				{ name: 'inviteUser', title: 'Invite User' },
				{ name: 'quitRoom', title: 'Quit Room' },
				// { name: 'deleteRoom', title: 'Delete Room' },
			],
			menuActions: [
				{ name: 'inviteUser', title: 'Invite User' },
				{ name: 'quitRoom', title: 'Quit Room' },
				// { name: 'deleteRoom', title: 'Delete Room' },
			],
			messages: [],
			messagePage: 0,
			messagesLoaded: false,
			templatesText: [
				{
					tag: 'help',
					text: 'This is the help'
				},
				{
					tag: 'action',
					text: 'This is the action'
				}
			],
			showFooter: true,
			showPopUpInfoRoomWith2Members: false,
			showPopUpInfoRoomWithMembers: false,
			showPopUpInviteUserToRoom: false,
			displayedDate: '',
			emojiDataSource: "https://cdn.jsdelivr.net/npm/emoji-picker-element-data@%5E1/en/emojibase/data.json"
		}
	},

	created() {
		this.getCurrentUserId();
		this.fetchMoreRooms();
		this.subscribeSpecificUserWebSocket();
		document.addEventListener("visibilitychange", () => {
			if (document.hidden) {
				console.log("Tab is changed");
				// Not done
			} else {
				console.log("Tab is active");
				// Not done
			}
		});
	},

	methods: {
		formattedBirthday() {
			if (this.userFound && this.userFound.birthDay) {
				const parsedDate = parseISO(this.userFound.birthDay);
				this.displayedDate = format(parsedDate, "dd 'tháng' MM, yyyy", { locale: viLocale });
			}
		},
		async fetchMessages({ room = {}, options = {} }) {
			console.log(this.roomActions);
			var menuActionList = null;
			if (room.users.length === 2) {
				console.log("This is private chat");
				menuActionList = [{ name: 'callUser', title: 'Call User' }];
			}
			else {
				console.log("This is room chat");
				menuActionList = [
					{ name: 'inviteUser', title: 'Invite User' },
					{ name: 'quitRoom', title: 'Quit Room' },
					// { name: 'deleteRoom', title: 'Delete Room' },
				]
			}
			this.menuActions = menuActionList;

			console.log("Call fetchMessages");
			console.log("Option messages: ");
			console.log(options);
			console.log("Room info: ");
			console.log(room);
			room.unreadCount = 0;
			const roomId = room.roomId;
			this.currentRoom = roomId;
			this.messagesLoaded = false;
			try {
				if (options.reset) {
					console.log("Options is reset");
					this.messagePage = 0;
					this.messages = []

				} else {
					console.log("Options is not reset");
					// Not done

				}
			} catch (err) {
				console.log(err.toString())
			}
			const result = await axios.get(`http://localhost:8181/v1/chat/get-messages/${roomId}`, {
				params: {
					page: this.messagePage++,
					size: 20
				}
			});
			// console.log("Log first");
			console.log("Result: ");
			console.log(result.data);
			const messages = result.data.chatMessageResponses;
			messages.forEach(element => {
				element.files.forEach(file => {
					delete file.progress;
				})
			});
			console.log(messages);
			this.messages = [...messages, ...this.messages];
			if (result.data.totalPages === result.data.currentPage + 1 || result.data.totalPages === 0) {
				this.messagesLoaded = true;
			}
			console.log("Room actions");
			console.log(this.roomActions);
			// console.log(this.messages)
		},

		async fetchMessagesFromFakeServer({ room = {}, options = {} }) {
			console.log("Call fetchMessages");
			console.log("Option messages: ");
			console.log(options);
			console.log("Room info: ");
			console.log(room);
			room.unreadCount = 0;
			this.messagesLoaded = false;
			try {
				if (options.reset) {
					console.log("Options is reset");
					this.messagePage = 0;
					this.messages = []

				} else {
					console.log("Options is not reset");
					// Not done

				}
			} catch (err) {
				console.log(err.toString())
			}
			const result = await axios.get(`http://localhost/FakeApiChatApp/message.json`);
			// console.log("Log first");
			console.log("Result: ");
			console.log(result.data);
			const messages = result.data;
			// console.log(messages);
			this.messages = [...messages, ...this.messages];
			this.messagesLoaded = true;
			// console.log(this.messages)
		},

		async fetchMoreRooms() {
			console.log("Call fetchRooms");
			this.rooms = [];
			this.roomsLoaded = false;
			this.loadingRooms = true;
			try {
				const result = await axios.get('http://localhost:8181/v1/chat/get-rooms');
				// console.log("Log first");
				console.log("Result: ");
				console.log(result.data);
				this.rooms = [...this.rooms, ...result.data.getAllRoomResponses];
			} catch (err) {
				console.log(err.toString())
			}
			this.loadingRooms = false;
			this.roomsLoaded = true;
		},

		async editMessage({ roomId, messageId, newContent, files, replyMessage, usersTag }) {
			console.log("Call edit message");
			console.log("RoomId", roomId);
			console.log("MessageId", messageId);
			console.log("New Content", newContent);
			console.log("Files", files);
			console.log("Reply Message", replyMessage);
			console.log("Users Tag", usersTag);
			const message = this.messages.find(message => message._id == messageId);
			const oldMessage = message;
			if (newContent) {
				message.content = newContent;
			}
			if (files) {
				console.log(files);
				message.files = files;
			}
			if (usersTag) {
				message.taggedUser = usersTag;
			}
			try {
				const result = await this.callApiUpdateMessage(roomId, message);
				console.log(result);
				if (result.status === 200) {
					console.log("Update message successfully");
				} else {
					message.content = oldMessage.content;
					message.files = oldMessage.files;
					message.taggedUser = oldMessage.taggedUser;
				}
			} catch (exception) {
				console.log(exception);
			}
		},

		async callApiUpdateMessage(roomId, message) {
			// const form = new FormData();
			// form.append('roomId', 1);
			// console.log("Message reactions: ");
			// console.log(message.reactions);
			console.log("Message ");
			console.log(message);
			console.log("Reply Message Id", message.replyMessage);
			console.log("Files", message.files);
			const form = new FormData();
			form.append('roomId', roomId);
			form.append('messageId', message._id);
			form.append('content', message.content);
			form.append('system', message.system);
			form.append('saved', message.saved);
			form.append('distributed', message.distributed);
			form.append('seen', message.seen);
			form.append('failure', message.failure);
			form.append('disableActions', message.disableActions);
			form.append('disableReactions', message.disableReactions);
			form.append('reactions', JSON.stringify(message.reactions));
			console.log("Form");
			console.log(form.get('replyMessageId'));
			const files = message.files;
			if (files) {
				console.log("Test file");
				for (const file of files) {
					console.log("File");
					console.log(file);
					const blob = !file.blob ? await fetch(file.url).then(r => r.blob()) : [];
					console.log(blob);
					const convertFile = new File([file.blob ? file.blob : blob], file.type === 'audio/mp3' ? file.name : (file.extension ? file.name.concat('.').concat(file.extension) : file.name.concat('.').concat(file.type)), {
						type: file.type
					});
					console.log("Convert file");
					console.log(convertFile);
					form.append('files', convertFile);
				}
			} else {
				form.append('files', null);
			}

			console.log("File form");
			console.log(form.get('files'));
			const result = await axios.put(`http://localhost:8181/v1/chat/update-message`, form, {
				headers: {
					'Content-Type': 'multipart/form-data'
				}
			});
			console.log(result.data);
			return result;
		},
		async sendMessage({ roomId, content, files, replyMessage, usersTag }) {
			console.log("Call send message");
			console.log("RoomId: ", roomId);
			console.log("Content: ", content);
			console.log("Files: ", files);
			console.log("ReplyMessage: ", replyMessage);
			console.log("User tag:", usersTag);
			const form = new FormData();
			form.append('roomId', roomId);
			form.append('content', content);
			form.append('system', false);
			form.append('saved', false);
			form.append('distributed', false);
			form.append('seen', false);
			form.append('failure', false);
			form.append('disableActions', false);
			if (files) {
				files.forEach(file => {
					const convertFile = new File([file.blob], file.type === 'audio/mp3' ? file.name : (file.extension ? file.name.concat('.').concat(file.extension) : file.name.concat('.').concat(file.type)), {
						type: file.type
					}
					);
					form.append('files', convertFile);
				});
			}
			if (replyMessage)
				form.append('replyMessageId', replyMessage ? parseInt(replyMessage._id) : null);
			console.log("Form");
			console.log(form.get('files'));
			try {
				const result = await axios.post(`http://localhost:8181/v1/chat/create-message`, form, {
					headers: {
						'Content-Type': 'multipart/form-data'
					}
				});
				if (result.status === 200) {
					console.log(result);
					result.data.chatMessageResponse.saved = true;
					result.data.chatMessageResponse.distributed = true;
					if (files) {
						result.data.chatMessageResponse.files.forEach(file => {
							delete file.progress;
						});
					}
				} else {
					result.data.chatMessageResponse.failure = true;
				}
				this.messages = [...this.messages, result.data.chatMessageResponse];
				// const room 
			} catch (exception) {
				console.log("Loi roi");
				console.log(exception);
			}
		},

		async sendMessageReaction({ roomId, messageId, reaction, remove }) {
			console.log("Call send message reaction");
			console.log("Room Id: ", roomId);
			console.log("Message Id: ", messageId);
			console.log("Reaction: ", reaction);
			console.log("Remove: ", remove);
			const message = this.messages.find(message => message._id == messageId);
			console.log(message);
			if (!message) return;
			const reactionUser = Object.prototype.hasOwnProperty.call(message.reactions, reaction.unicode) ? message.reactions[reaction.unicode] : [];
			console.log("Reaction");
			console.log(reactionUser);

			if (!reactionUser.includes(this.currentUserId.toString()))
				reactionUser.push(this.currentUserId.toString());
			else if (remove === true) {
				const index = reactionUser.indexOf(this.currentUserId.toString());
				reactionUser.splice(index, 1);
			}
			try {
				const oldReactions = message.reactions;
				message.reactions[reaction.unicode] = reactionUser
				const result = await this.callApiUpdateMessage(roomId, message);
				if (result.status !== 200) {
					console.log("Error");
					message.reactions = oldReactions;
				}
			} catch (error) {
				console.log(error);
			}
		},

		async deleteMessage({ roomId, message }) {
			console.log("Call delete message");
			console.log("RoomId:", roomId)
			console.log("Message: ", message);
			try {
				const result = await axios.delete(`http://localhost:8181/v1/chat/delete-message/${message._id}`).catch(function (error) {
					console.log(error);
				})
				if (result.status === 200) {
					console.log("Set message deleted");
					// message.deleted = true;
					console.log(this.messages);
					const currentMessage = this.messages.find(currentMessage => currentMessage._id == message._id);
					currentMessage.deleted = true;
				}
			} catch (exception) {
				console.log(exception);
			}
		},
		async showRoomInfo(room) {
			console.log("Call show room info");
			console.log(room);
			try {
				const result = await axios.get(`http://localhost:8181/v1/chat/get-room-info/${room.roomId}`);
				if (result.status === 200) {
					console.log(result);
					console.log(result.data.getRoomInfo.users);
					this.roomDetail = result.data.getRoomInfo;
					console.log(this.roomDetail);
					if (this.roomDetail.membersCount == 2) {
						console.log(this.roomDetail.users[0]);
						await this.getUserInfo(this.roomDetail.users[0]._id);
						this.formattedBirthday();
						this.showPopUpInfoRoomWith2Members = true;
					} else {
						this.showPopUpInfoRoomWithMembers = true;
					}
				}
				// const room 
			} catch (exception) {
				console.log("Loi roi");
				console.log(exception);
			}
		},
		async getUserInfo(userId) {
			try {
				const responseUser = await axios.get(`users/findUserById/${userId}`, {
					headers: {
						'Authorization': localStorage.getItem("token")
					}
				});

				if (responseUser.status === 200) {
					console.log(responseUser.data);
					this.userFound = responseUser.data;
				} else {
					console.error(responseUser.body);
					this.toast.error(responseUser.body || 'Đã xảy ra lỗi!', { timeout: 1500 });
				}
			} catch (error) {
				if (error.response) {
					if (error.response.status === 400) {
						this.toast.error(error.response.data, { timeout: 1500 });
					} else {
						this.toast.error(error.response.data, { timeout: 1500 });
					}
				} else if (error.request) {
					this.toast.error('Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!', { timeout: 1500 });
				} else {
					this.toast.error('Error setting up the request:' + error.message, { timeout: 1500 });
				}
			}
		},
		closePopupInfoRoom() {
			this.showPopUpInfoRoomWith2Members = false;
			this.showPopUpInfoRoomWithMembers = false;
		},

		roomActionHandler({ roomId, action }) {
			console.log("Call roomActionHandler function");
			console.log("RoomId", roomId);
			console.log("Action", action);
		},

		menuActionHandler({ roomId, action }) {
			console.log("Call menuActionHandler function");
			console.log("RoomId", roomId);
			console.log("Action", action);
			const room = this.rooms.find(room => room.roomId === roomId);
			if (action.name === 'callUser') {
				this.callToSpecificUser(room);
			}
			if (action.name === 'quitRoom') {
				this.quitRoom(room);
			} if (action.name === 'inviteUser') {
				this.inviteUser(room);
			}
		},

		async getListOfFriends(room) {
			console.log("Gọi hàm: getListOfFriends");
			try {
				const response = await axios.get(`users/getListFriendsAreNotInRoom/${room.roomId}`, {
					headers: {
						'Authorization': localStorage.getItem("token")
					}
				});

				if (response.status === 200) {
					this.listFriends = response.data;
				} else {
					console.error(response.data);
					this.toast.error(response.data, { timeout: 1500 });
				}
			} catch (error) {
				if (error.response) {
					if (error.response.status === 400) {
						this.toast.error(error.response.data, { timeout: 1500 });
					} else {
						this.toast.error(error.response.data, { timeout: 1500 });
					}
				} else if (error.request) {
					this.toast.error('Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!', { timeout: 1500 });
				} else {
					this.toast.error('Error setting up the request:' + error.message, { timeout: 1500 });
				}
			}
		},
		deleteFriendByPhoneNumber(phoneNumber) {
			console.log("Gọi hàm: deleteFriendByPhoneNumber");
			this.listFriends = this.listFriends.filter(friend => friend.phoneNumber !== phoneNumber);
		},
		addFriendTag(friend) {
			this.addedFriends.push(friend);
			this.deleteFriendByPhoneNumber(friend.phoneNumber);
		},
		deleteFriendTag(friend) {
			const indexInTag = this.addedFriends.findIndex(taggedFriend => taggedFriend.phoneNumber === friend.phoneNumber);
			if (indexInTag !== -1) {
				this.addedFriends.splice(indexInTag, 1);
			}

			const indexInFriends = this.listFriends.findIndex(existingFriend => existingFriend.phoneNumber === friend.phoneNumber);
			if (indexInFriends === -1) {
				this.listFriends.push(friend);
			}
		},
		callToSpecificUser(room) {
			console.log("Call function callToSpecificUser");
			console.log("Room info");
			console.log(room);
			const user = room.users.filter(element => element._id != this.currentUserId);
			this.initializeRTCPeerConnection(user[0]._id);
			this.createOffer(user[0]._id);
		},

		async inviteUser(room) {
			try {
				console.log(room);
				await this.getListOfFriends(room);
				this.currentRoomInfo = room;
				this.showPopUpInviteUserToRoom = true;
			} catch (exception) {
				console.log("Loi roi");
				console.log(exception);
			}
		},

		async addUsersToGroup() {
			try {
				console.log(this.currentRoomInfo);
				const usersPhoneNumber = this.addedFriends.map(friend => friend.phoneNumber);
				console.log(usersPhoneNumber);
				const result = await axios.post(`http://localhost:8181/v1/chat/add-users-to-room/${this.currentRoomInfo.roomId}`, usersPhoneNumber);
				if (result.status === 200) {
					console.log(result);
					this.toast.success("Thêm người dùng vào nhóm thành công!", { timeout: 1500 });
					this.closeChooseFriendDialog();
					this.fetchMoreRooms();
				} else {
					this.toast.error(result.data, { timeout: 1500 });
				}
			} catch (exception) {
				console.log("Loi roi");
				console.log(exception);
			}
		},
		closeChooseFriendDialog() {
			this.showPopUpInviteUserToRoom = false;
			this.addedFriends = [];
		},

		async quitRoom(room) {
			if (confirm("Bạn có chắc muốn rời nhóm?")) {
				console.log(room);
				try {
					const result = await axios.delete(`http://localhost:8181/v1/chat/delete-room/${room.roomId}`);
					if (result.status === 200) {
						console.log(result);
						this.toast.success("Rời khỏi nhóm thành công!", { timeout: 1500 });
						this.fetchMoreRooms();
					} else {
						this.toast.error(result.data.message, { timeout: 1500 });
					}
					// const room 
				} catch (exception) {
					console.log("Loi roi");
					console.log(exception);
				}
			}
		},

		getCurrentUserId() {
			const user = JSON.parse(localStorage.getItem('user'));
			this.currentUserId = user.id;
			console.log(this.currentUserId);
		},

		async fetchNewInfoRoom(roomId) {
			console.log("Call fetchNewInfoRoom " + roomId);
		},

		async fetchNewInfoMessage(roomId) {
			console.log("Call fetchNewInfoMessage" + roomId);
		},

		handleNewUpdate(message) {
			console.log("Call handle new update");
			const notification = JSON.parse(message.body);
			if (notification.typeNotification === "RTC_CONNECTION") {
				if (notification.message) {
					if (notification.message.event === 'offer') {
						if (this.RTCConnectionList.indexOf(this.currentUserId === 1 ? 2 : 1) === -1)
							this.initializeRTCPeerConnection(this.currentUserId === 1 ? 2 : 1);
						this.handleOffer(notification.message.data)
					} else if (notification.message.event === 'answer') {
						this.handleAnswer(notification.message.data);
					} else if (notification.message.event === 'candidate') {
						this.handleCandidate(notification.message.data);
					}
				}
			} else {
				if (this.currentRoom == notification.roomId) {
					if (notification.message.files) {
						notification.message.files.forEach(file => {
							delete file.progress;
						});
					}
					if (notification.typeNotification === "CREATE") {
						this.messages = [...this.messages, notification.message];
					} else if (notification.typeNotification === "UPDATE") {
						console.log("Message is update");
						let message = this.messages.find(message => message._id == notification.message._id);
						console.log(message);
						if (message) {
							const indexMessage = this.messages.indexOf(message);
							this.messages[indexMessage] = notification.message;
							this.messages = [...this.messages];
						}
					}
				} else {
					// fetch new info room
				}
			}
		},

		// --------------Config web socket--------------

		async subscribeSpecificUserWebSocket() {
			this.socket = new SockJS('http://localhost:8181/room');
			this.stompClient = Stomp.over(this.socket);

			// var sessionId = "";
			var userId = JSON.parse(localStorage.getItem('user'))['id'];

			await this.stompClient.connect({ userId: 'user' + userId }, frame => {
				console.log("Frame");
				console.log(frame);
				if (this.stompClient.connected) {
					console.log("Connected success to server");
					this.stompClient.subscribe('/user/topic/specific-user', this.handleNewUpdate);
				}
				console.log(this.stompClient.connected);
				// this.stompClient.send("/app/room", "Hehehe", { userId: 'user' + 2 });
			}, this.handleErrorSubscribe);
			this.stompClient.onMessage = message => this.handleNewUpdate(message);
		},

		async initializeRTCPeerConnection(userId) {
			console.log("Call initializeRTCPeerConnection");
			var configuration = null;

			this.peerConnection = new RTCPeerConnection(configuration);

			// Setup ice handling
			this.peerConnection.onicecandidate = event => {
				if (event.candidate) {
					this.send(JSON.stringify({
						event: "candidate",
						data: event.candidate
					}), userId);
				}
			};

			// creating data channel
			this.dataChannel = this.peerConnection.createDataChannel("dataChannel", {
				reliable: true
			});

			this.dataChannel.onerror = function (error) {
				console.log("Error occured on datachannel:", error);
			};

			// when we receive a message from the other peer, printing it on the console
			this.dataChannel.onmessage = function (event) {
				console.log("message:", event.data);
			};

			this.dataChannel.onclose = function () {
				console.log("data channel is closed");
			};

			this.peerConnection.ondatachannel = function (event) {
				this.dataChannel = event.channel;
			};
		},

		createOffer(userId) {
			this.peerConnection.createOffer(offer => {
				this.send(JSON.stringify({
					event: "offer",
					data: offer
				}), userId);
				this.peerConnection.setLocalDescription(offer);
			}, function (error) {
				console.log(error);
				alert("Error creating an offer");
			});
		},

		handleOffer(offer) {
			console.log("Call handle offer");
			console.log("Offer");
			console.log(offer);
			this.peerConnection.setRemoteDescription(new RTCSessionDescription(offer));

			// create and send an answer to an offer
			this.peerConnection.createAnswer(answer => {
				this.peerConnection.setLocalDescription(answer);
				this.send(JSON.stringify({
					event: "answer",
					data: answer
				}), 1);
			}, function (error) {
				console.log(error);
				alert("Error creating an answer");
			});

		},

		handleCandidate(candidate) {
			console.log("Call handle candidate");
			console.log(candidate);
			this.peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
		},

		handleAnswer(answer) {
			this.peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
			console.log("connection established successfully!!");
			this.RTCConnectionList.push(this.currentUserId === 1 ? 2 : 1);
			console.log(this.RTCConnectionList);
		},

		send(object, userId) {
			console.log("Call send function", object);
			this.stompClient.send("/app/room", object, { userId: 'user' + userId, userSend: 'user' + this.currentUserId });
		},

		sendMessageRTC() {
			this.dataChannel.send("Test");
		},

		handleErrorSubscribe() {
			console.log("Have error when subscribe websocket");
		},

		// --------------End config websocket--------------

		handleUserChangeTab() {
			return function () {
				window.onfocus = window.onblur = window.onpageshow = window.onpagehide = function (e) {
					if ({ focus: 1, pageshow: 1 }[e.type]) {
						console.log("User is in current tab");
					} else {
						console.log("User is not in current tab");
					}
				};
			};
		}
	}
}
</script>

<style lang="scss">
body {
	font-family: 'Quicksand', sans-serif;
}

.dialog-container-user {
	.dialog-component-user {
		.dialog-title-user {
			.title-user {
				display: flex;
				justify-content: space-between;
				align-items: center;

				.icon-close-user {
					cursor: pointer;
				}
			}
		}

		.dialog-content-user {
			padding: 0;
			display: flex;
			flex-direction: column;
			position: relative;
			height: auto;

			.profile-photo-user {
				position: relative;
				display: block;

				.cover-avatar-user {
					position: relative;
					display: table;
					margin: auto;

					.cover-image-user {
						cursor: pointer;
						width: 352px;
						height: 152px;
						object-fit: cover;
					}
				}

				.ava-name-container-user {
					text-align: center;
					left: 0;
					width: 100%;
					color: #fff;
					position: relative;
					display: flex;
					background: transparent;
					margin-top: -48px;
					flex-direction: column;

					.avatar-profile-user {
						cursor: pointer;
						align-self: center;
						display: block;

						.avatar-user {
							width: 80px;
							height: 80px;
							min-width: 80px;
							min-height: 80px;
							position: relative;
							color: #fff;
							overflow: hidden;
							box-sizing: border-box;

							.avatar-image-user {
								display: flex;
								justify-content: center;
								align-items: center;
								background: #e6e8ea;
								color: #7589a3;
								overflow: hidden;
								object-fit: cover;
								font-size: 1.25rem;
								font-weight: 500;
								line-height: 1.5;
								border-width: 2px;
								box-sizing: border-box;
								border-radius: 50%;
								border: 2px solid #fff;
								width: 80px;
								height: 80px;
								min-width: 80px;
								min-height: 80px;
								overflow-clip-margin: content-box;
							}
						}
					}

					.fullname-profile-user {
						width: 100%;
						color: #081c36;
						font-size: 1.125rem; // Thay đổi font-size thành 1.125rem
						font-weight: 500; // Thay đổi font-weight thành 500
						line-height: 1.5;
						display: flex;
						flex-direction: column;
						z-index: 1;
						margin-top: 10px;

						.fullname-user {
							position: relative;
							width: fit-content;
							margin: auto;
							max-width: 271px;
							text-overflow: ellipsis;
						}
					}
				}
			}

			.profile-information-user {

				.profile-header-user {
					margin-top: 5px;
					margin-bottom: 10px;
					margin-left: 3px;
				}

				.user-profile-details-user {
					display: flex;
					flex-direction: column;

					.user-profile-item-user {
						display: flex;
						justify-content: space-between;
						margin-bottom: 10px;

						.title-user {
							flex-basis: 40%;
							margin-left: 3px;
						}

						.content-user {
							flex-basis: 60%;
						}

						.title-room {
							flex-basis: 60%;
							margin-left: 3px;
						}

						.content-room {
							flex-basis: 25%;
						}
					}
				}
			}

			.profile-action-user {
				height: 40px;
				display: flex;
			}
		}

	}

	.block-button,
	.add-friend-button {
		padding: 8px 16px;
	}
}

.friend-list-container {
	height: 250px;
	overflow-y: auto;
	overflow-x: hidden;
	border: 1px solid #ccc;
	border-radius: 8px;
}

.dialog-content {
	padding: 0;
	display: flex;
	flex-direction: column;
	position: relative;

	.text-sea-green {
		color: #0077cc;
	}

	.text-leaf-green {
		color: #00aa33;
	}

	.avatar-container {
		max-height: 80px;
		display: flex;
		align-items: center;
		margin-left: 14px;
		margin-right: 4px;
	}

	.avatar-wrapper {
		width: 50px !important;
		height: 50px !important;
		display: flex;
		justify-content: center;
		align-items: center;
		overflow: hidden;
		border-radius: 50%;
		margin-right: 16px;
	}

	.avatar {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}

	.detail {
		flex: 1;

		.form-control {
			background-color: #cccc;
			border-radius: 5%;
			text-align: center;
		}
	}

	.friend-info {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.profile-action {
		height: 40px;
		display: flex;
	}
}

.dialog-container {

	width: 500px;

	.dialog-component {
		.dialog-title {
			.title {
				display: flex;
				justify-content: space-between;
				align-items: center;

				.icon-close {
					cursor: pointer;
				}
			}
		}

		.dialog-content {
			padding: 0;
			display: flex;
			flex-direction: column;
			position: relative;
			height: auto;

			.text-sea-green {
				color: #0077cc;
			}

			.text-leaf-green {
				color: #00aa33;
			}

			.avatar-container {
				height: 80px;
				display: flex;
				align-items: center;
				margin-left: 14px;
			}

			.avatar-wrapper {
				width: 50px;
				height: 50px;
				display: flex;
				justify-content: center;
				align-items: center;
				overflow: hidden;
				border-radius: 50%;
				margin-right: 16px;
			}

			.avatar {
				width: 100%;
				height: 100%;
				object-fit: cover;
			}

			.detail {
				display: flex;
				flex-direction: column;
				justify-content: center;

				.form-control {
					background-color: #cccc;
					border-radius: 5%;
					text-align: center;
				}
			}

			.friend-info {
				display: flex;
				align-items: center;
			}

			.profile-action {
				height: 40px;
			}
		}
	}

	.upload-file {
		width: 372px;
		height: 200px;
	}

	.image-container {
		position: relative;
		display: inline-block;
	}

	.overlay {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: rgba(0, 0, 0, 0.5);
		color: #fff;
		font-size: 24px;
	}

	.position-relative {
		position: relative;
	}

	.close-icon {
		position: absolute;
		top: 8px;
		right: 8px;
		width: 30px;
		height: 30px;
		background-color: white;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}

	.close-icon font-awesome-icon {
		color: gray;
		font-size: 16px;
	}

	.friend-container {
		transition: box-shadow 0.3s, transform 0.3s;
		overflow-x: hidden;
		cursor: pointer;
	}

	.friend-container:hover {
		box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);
		transform: scale(1.05);
	}

	.search-input {
		margin-top: 8px;
		padding: 4px;
		width: 100%;
		box-sizing: border-box;
	}

	.friend-list-container {
		overflow-x: hidden;
		height: 250px;
		overflow-y: auto;
		border: 1px solid #ccc;
		border-radius: 8px;
	}

	.update-file-container {
		height: 400px;
		overflow-x: hidden;
		overflow-y: scroll;
		border: 1px solid #ccc;
		border-radius: 8px;
	}

	.file-list-container {
		overflow-y: auto;
		overflow-x: hidden;
	}

	.color-blue {
		color: #007BFF;
	}
}

.position-relative {
	position: relative;
}
</style>