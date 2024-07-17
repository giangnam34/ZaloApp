<template>
	<div>
		<vue-advanced-chat height="calc(100vh - 20px)" :current-user-id="currentUserId" :rooms="JSON.stringify(rooms)"
			:loading-rooms="loadingRooms" @fetch-more-rooms="fetchMoreRooms" :rooms-loaded="roomsLoaded"
			:room-actions="JSON.stringify(roomActions)" :menu-actions="JSON.stringify(menuActions)"
			:messages="JSON.stringify(messages)" :messages-loaded="messagesLoaded" :load-first-room="loadFirstRoom"
			:show-footer="showFooter" @send-message="sendMessage($event.detail[0])" :room-info-enabled="roomInfo"
			:show-new-messages-divider="true" @fetch-messages="fetchMessages($event.detail[0])"
			:templates-text="JSON.stringify(templatesText)" @delete-message="deleteMessage($event.detail[0])"
			:accepted-files="acceptedFiles" @send-message-reaction="sendMessageReaction($event.detail[0])"
			:theme="theme" @room-info="showRoomInfo($event.detail[0])" @edit-message="editMessage($event.detail[0])"
			@room-action-handler="roomActionHandler($event.detail[0])" @add-room="addRoom()"
			@menu-action-handler="menuActionHandler($event.detail[0])" :emoji-data-source="emojiDataSource"
			@open-file="openFileHandler($event.detail[0])">
		</vue-advanced-chat>
		<v-dialog class="dialog-container-user" v-model="showPopUpInfoRoomWith2Members" max-width="352px"
			@click:outside="closePopupInfoRoom">
			<v-card class="dialog-component-user">
				<v-card-title class="dialog-title-user">
					<h2 class="title-user">Info account
						<div class="icon-close-user" @click="closePopupInfoRoom"><font-awesome-icon
								icon="fa-solid fa-x" />
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
						<div class="icon-close-user" @click="closePopupInfoRoom"><font-awesome-icon
								icon="fa-solid fa-x" />
						</div>
					</h2>
				</v-card-title>
				<hr style="border: none; border-bottom: 1px solid #ccc;">
				<v-card-text class="dialog-content-user">
					<div class="profile-photo-user">
						<div class="cover-avatar-user">
							<img class="cover-image-user" :src="roomDetail.roomAvatar" alt="None"
								crossorigin="anonymous">
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
					<h2 class="title">Add friend to group
						<div class="icon-close" @click="closeChooseFriendDialog"><font-awesome-icon
								icon="fa-solid fa-x" />
						</div>
					</h2>
				</v-card-title>
				<hr style="border: none; border-bottom: 1px solid #ccc;">
				<v-card-text class="dialog-content">
					<div class="pt-4 pl-4 pr-4">
						<input type="text" v-model="searchText" placeholder="Find by user name" class="search-input" />
						<div v-if="addedFriends.length !== 0"><span>Add to group ({{ addedFriends.length }}
								friend
								)</span></div>
						<div class="update-file-container" style="height:100px" v-if="addedFriends.length !== 0">
							<div v-for="friend in addedFriends" v-bind:key="friend.phoneNumber"
								class="position-relative">
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
						<div><span>List of friend ({{ listFriends.length }} friend)</span></div>
						<div class="friend-list-container">
							<div v-for="friend in filteredFriendsForInvite" v-bind:key="friend.phoneNumber"
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
							Add to group
						</div>
					</div>
				</v-card-text>
			</v-card>
		</v-dialog>
		<v-dialog class="dialog-container" v-model="showPopUpCreateRoom" max-width="500px"
			@click:outside="closeCreateRoomDialog">
			<v-card class="dialog-component">
				<v-card-title class="dialog-title">
					<h2 class="title">Create new group
						<div class="icon-close" @click="closeCreateRoomDialog"><font-awesome-icon
								icon="fa-solid fa-x" />
						</div>
					</h2>
				</v-card-title>
				<hr style="border: none; border-bottom: 1px solid #ccc;">
				<v-card-text class="dialog-content">
					<div class="create_group_header">
						<div class="relative">
							<div style="top: 57px; left: -1px; position: absolute; pointer-events: none;"></div>
							<div class="create_group_avatar">
								<input type="file" @change="handleFileChange" ref="groupAvatarFile"
									style="display: none;" />
								<div v-if="groupAvatarFile === null" @click="getFileGroupAvatar"
									class="cursor-pointer flex justify-center items-center w-100 h-100">
									<font-awesome-icon icon="fa-solid fa-camera" />
								</div>
								<div v-else @click="getFileGroupAvatar">
									<img :src="getUrl(groupAvatarFile)" alt="Selected Image" class="cursor-pointer" />
								</div>
							</div>
						</div>
						<div class="create_group_input">
							<span class="span_input">
								<input type="text" placeholder="Enter group name..." maxlength="50" class="input_name"
									v-model="groupName">
							</span>
						</div>
					</div>
					<div class="pt-4 pl-4 pr-4">
						<input type="text" v-model="searchText" placeholder="Find by user name" class="search-input" />
						<div v-if="addedFriends.length !== 0"><span>Add to group ({{ addedFriends.length }}
								friend
								)</span></div>
						<div class="update-file-container" style="height:100px" v-if="addedFriends.length !== 0">
							<div v-for="friend in addedFriends" v-bind:key="friend.phoneNumber"
								class="position-relative">
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
						<div><span>List of friend ({{ listFriendsForCreateRoom.length }} friend)</span></div>
						<div class="friend-list-container">
							<div v-for="friend in filteredFriendsForCreateRoom" v-bind:key="friend.phoneNumber"
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
					<div class="profile-action" @click="confirmCreateRoom"
						:class="{ 'disabled': !checkValidForCreateRoom }">
						<div class="items-center text-center cursor-pointer bg-blue-400 rounded-lg h-10 mx-4 px-4 py-2">
							Create group
						</div>
					</div>
				</v-card-text>
			</v-card>
		</v-dialog>

		<template>
			<div>
				<!-- <a-modal v-if="callIncoming" title="Incoming Call" ok-text="Accept" cancel-text="Decline" @ok="acceptCall"
					@cancel="declineCall">
					<p>You have an incoming call. Do you want to accept it?</p>
				</a-modal> -->

				<v-dialog v-model="videoCallDialog" max-width="90%" max-height="90%" persistent class="custom-dialog">
					<v-card>
						<v-card-title class="headline">Video Call</v-card-title>
						<v-card-text>
							<video-call-component :room="currentRoomVideoCall" :localStream="localStream"
								:remoteStream="remoteStream" @endCall="endCall" />
						</v-card-text>
					</v-card>
				</v-dialog>
			</div>
		</template>
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
import VideoCallComponent from './VideoCallComponent.vue';
import { Modal } from 'ant-design-vue';
import newMessageSound from '../assets/messageNotificationSound.mp3';
// import { register } from '../../vue-advanced-chat/dist/vue-advanced-chat.es.js'
register()

var audio = new Audio(newMessageSound);
let originalTitle = "NathApp";
let blinkInterval;
let isBlinking = false;

export default {
	components: {
		// UserInfo
		VideoCallComponent
	},
	computed: {
		filteredFriendsForInvite() {
			const normalizedSearchText = this.searchText.toLowerCase();
			return this.listFriends.filter(friend =>
				friend.userName.toLowerCase().includes(normalizedSearchText)
			);
		},
		filteredFriendsForCreateRoom() {
			const normalizedSearchText = this.searchText.toLowerCase();
			return this.listFriendsForCreateRoom.filter(friend =>
				friend.userName.toLowerCase().includes(normalizedSearchText)
			);
		},
		hasSystemMessage() {
			return this.messages.some(message => message.system && message.content.includes('Không thể tiếp tục gửi tin nhắn do đã bị chặn từ người dùng'));
		},
		checkValidForCreateRoom() {
			return this.addedFriends.length >= 2 && this.groupAvatarFile !== null && this.groupName !== "";
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
			callIncoming: false,
			offerData: null,
			remoteUser: null,
			localStream: null,
			remoteStream: null,
			videoCallDialog: false,
			peerConnection: null,
			dataChannel: null,
			socket: null,
			stompClient: null,
			currentUserId: null,
			currentUser: null,
			userFound: null,
			listFriends: [],
			addedFriends: [],
			searchText: "",
			groupName: "",
			theme: 'light',
			currentRoomId: null,
			currentRoomInfo: null,
			RTCConnectionList: [],
			rooms: [],
			roomDetail: null,
			roomsLoaded: false,
			loadingRooms: true,
			roomForNotification: null,
			loadFirstRoom: true,
			roomInfo: true,
			showFooter: true,
			acceptedFiles: "image/*, video/*",
			roomActions: [
				// { name: 'inviteUser', title: 'Invite User' },
				// { name: 'quitRoom', title: 'Quit Room' },
				// { name: 'deleteRoom', title: 'Delete Room' },
			],
			menuActions: [
				{ name: 'inviteUser', title: 'Invite User' },
				{ name: 'quitRoom', title: 'Quit Room' },
				// { name: 'deleteRoom', title: 'Delete Room' },
			],
			constraints: {
				video: {
					frameRate: {
						ideal: 10,
						max: 15
					},
					width: 200,
					height: 200,
					facingMode: "user"
				},
				audio: true
			},
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
			showPopUpInfoRoomWith2Members: false,
			showPopUpInfoRoomWithMembers: false,
			showPopUpInviteUserToRoom: false,
			showPopUpCreateRoom: false,
			listFriendsForCreateRoom: [],
			displayedDate: '',
			groupAvatarFile: null,
			emojiDataSource: "https://cdn.jsdelivr.net/npm/emoji-picker-element-data@%5E1/en/emojibase/data.json",
			timeoutId: null,
			callAccepted: false,
			timeout: null,
			callDeclined: false,
			confirmModal: null,
			user: null,
			receiverId: null,
		}
	},

	created() {
		document.title = "NathApp";
		this.getCurrentUserId();
		this.fetchMoreRooms();
		this.subscribeSpecificUserWebSocket();
		const userString = localStorage.getItem('user');
		if (userString) {
			this.user = JSON.parse(userString);
		}
		window.addEventListener("focus", async () => {
			try {
				this.messages = this.messages.map(message => {
					if (parseInt(message.senderId) !== parseInt(this.currentUserId) && message.seen == false) {
						const updatedMessage = {
							...message,
							seen: true
						};
						this.callApiUpdateMessage(this.currentRoomId, updatedMessage);

						return updatedMessage;
					}
					return message;
				});

				this.stopTitleBlinking();
				document.title = "NathApp";
			} catch (exception) {
				console.log(exception);
			}
			// console.log(this.messages);
			// this.messages.forEach(message => this.callApiUpdateMessage(this.currentRoom, message));
		});
	},
	watch: {
		callAccepted(newVal) {
			if (newVal && this.timeout) {
				clearTimeout(this.timeout);
				if (this.confirmModal) {
					this.confirmModal.destroy();
				}
			}
		},
		callDeclined(newVal) {
			if (newVal && this.timeout) {
				clearTimeout(this.timeout);
				if (this.confirmModal) {
					this.confirmModal.destroy();
				}
				this.videoCallDialog = false;
			}
		},
	},
	methods: {
		closeDialog() {
			try {
				console.log("closeDialog called"); // Kiểm tra xem phương thức có được gọi không
				if (this.localStream) {
					this.localStream.getTracks().forEach(track => {
						track.stop();
					});
				}
				this.videoCallDialog = false;
				clearTimeout(this.timeout);
			} catch (exception) {
				console.log("Error in closeDialog ", exception);
			}
		},
		formattedBirthday() {
			try {
				if (this.userFound && this.userFound.birthDay) {
					const parsedDate = parseISO(this.userFound.birthDay);
					this.displayedDate = format(parsedDate, "dd 'tháng' MM, yyyy", { locale: viLocale });
				}
			} catch (exception) {
				console.log("Error in formattedBirthday ", exception);
			}
		},
		typingMessage({ roomId, message }) {
			try {

				const roomIndex = this.rooms.findIndex(room => room.roomId === roomId);

				if (roomIndex !== -1) {
					this.rooms[roomIndex].typingUsers = [...this.rooms[roomIndex].typingUsers, this.currentUserId];
				}
			} catch (exception) {
				console.log("Error in typingMessage ", exception);
			}
		},
		async fetchMessages({ room = {}, options = {} }) {
			try {
				// console.log(this.roomActions);
				var menuActionList = null;
				if (room.users.length === 2) {
					// console.log("This is private chat");
					menuActionList = [
						{ name: 'callUser', title: 'Call' },
						{ name: 'blockUser', title: 'Block' },
						// { name: 'unFriendUser', title: 'Unfriend' }
					];
				}
				else {
					// console.log("This is room chat");
					menuActionList = [
						{ name: 'inviteUser', title: 'Invite User' },
						{ name: 'quitRoom', title: 'Quit Room' },
						// { name: 'deleteRoom', title: 'Delete Room' },
					]
				}
				this.menuActions = menuActionList;

				// console.log("Call fetchMessages");
				// console.log("Option messages: ");
				// console.log(options);
				// console.log("Room info: ");
				// console.log(room);
				room.unreadCount = 0;
				const roomId = room.roomId;
				this.currentRoomId = roomId;
				this.messagesLoaded = false;
				try {
					if (options.reset) {
						// console.log("Options is reset");
						this.messagePage = 0;
						this.messages = []

					} else {
						// console.log("Options is not reset");
						// Not done

					}
				} catch (err) {
					// console.log(err.toString())
				}
				const result = await axios.get(`http://localhost:8181/v1/chat/get-messages/${roomId}`, {
					params: {
						page: this.messagePage++,
						size: 20
					}
				}).catch(error => {
					console.log(error);
				});
				// console.log("Log first");
				console.log("Result: ");
				console.log(result.data);
				let messages = result.data.chatMessageResponses;
				let isBlock = false;
				messages = messages.filter(message => !(message.system === true && message.deleted === true));
				messages.forEach(element => {
					if (element.isBlock === true) {
						// element.content = "Tin nhắn đã bị ẩn!";
					}
					element.files.forEach(file => {
						delete file.progress;
					});
					if (element.content.includes("Không thể tiếp tục gửi tin nhắn do đã bị chặn từ người dùng") && element.system == true) {
						isBlock = true;
					}
				});
				// console.log(messages);
				this.messages = [...messages, ...this.messages];
				const checkShowFooter = this.messages.some(message => message.system && message.content.includes('Không thể tiếp tục gửi tin nhắn'));

				if (checkShowFooter) {
					this.showFooter = false;
					if (this.messages.some(message => message.system && message.content.includes('Không thể tiếp tục gửi tin nhắn do đã bị chặn từ người dùng'))) {
						menuActionList = [
							{ name: 'unblockUser', title: 'Unblock' },
							// { name: 'deleteRoom', title: 'Delete Room' },
						]
					}
					else {
						menuActionList = [
							// { name: 'sendInviteFriend', title: 'Send Invite Friend' },
							// { name: 'deleteRoom', title: 'Delete Room' },
						]
					}
					this.menuActions = menuActionList;
				} else {
					this.showFooter = true;
				}

				if (result.data.totalPages === result.data.currentPage + 1 || result.data.totalPages === 0) {
					this.messagesLoaded = true;
				}
				// console.log("Room actions");
				// console.log(this.roomActions);
				// // console.log(this.messages)
			} catch (exception) {
				console.log("Error in fetchMessages ", exception);
			}
		},

		async fetchMessagesFromFakeServer({ room = {}, options = {} }) {
			try {
				// console.log("Call fetchMessages");
				// console.log("Option messages: ");
				// console.log(options);
				// console.log("Room info: ");
				// console.log(room);
				room.unreadCount = 0;
				this.messagesLoaded = false;
				try {
					if (options.reset) {
						// console.log("Options is reset");
						this.messagePage = 0;
						this.messages = []

					} else {
						// console.log("Options is not reset");
						// Not done

					}
				} catch (err) {
					// console.log(err.toString())
				}
				const result = await axios.get(`http://localhost/FakeApiChatApp/message.json`).catch(error => {
					console.log(error);
				});
				// // console.log("Log first");
				// console.log("Result: ");
				// console.log(result.data);
				const messages = result.data;
				// // console.log(messages);
				this.messages = [...messages, ...this.messages];
				this.messagesLoaded = true;
				// // console.log(this.messages)
			} catch (exception) {
				console.log("Error in fetchMessagesFromFakeServer ", exception);
			}
		},

		async fetchMoreRooms() {
			try {
				// console.log("Call fetchRooms");
				this.rooms = [];
				this.roomsLoaded = false;
				this.loadingRooms = true;
				try {
					const result = await axios.get('http://localhost:8181/v1/chat/get-rooms').catch(error => {
						console.log(error);
					});
					// // console.log("Log first");
					// console.log("Result: ");
					// console.log(result.data);
					this.rooms = [...this.rooms, ...result.data.getAllRoomResponses];
				} catch (err) {
					// console.log(err.toString())
				}
				this.loadingRooms = false;
				this.roomsLoaded = true;
				console.log("All rooms");
				console.log(this.rooms);
				this.currentRoomId = this.rooms[0];
			} catch (exception) {
				console.log("Error in fetchMoreRooms", exception);
			}
		},

		async editMessage({ roomId, messageId, newContent, files, replyMessage, usersTag }) {
			try {
				// console.log("Call edit message");
				// console.log("RoomId", roomId);
				// console.log("MessageId", messageId);
				// console.log("New Content", newContent);
				// console.log("Files", files);
				// console.log("Reply Message", replyMessage);
				// console.log("Users Tag", usersTag);
				const message = this.messages.find(message => message._id == messageId);
				const oldMessage = message;
				if (newContent) {
					message.content = newContent;
				}
				if (files) {
					// console.log(files);
					message.files = files;
				}
				if (usersTag) {
					message.taggedUser = usersTag;
				}
				try {
					const result = await this.callApiUpdateMessage(roomId, message);
					// console.log(result);
					if (result.status === 200) {
						// console.log("Update message successfully");
					} else {
						message.content = oldMessage.content;
						message.files = oldMessage.files;
						message.taggedUser = oldMessage.taggedUser;
					}
				} catch (exception) {
					// console.log(exception);
				}
			} catch (exception) {
				console.log("Error in editMessage ", exception);
			}
		},

		playSound() {
			audio.play().catch(error => {
				console.log(error);
			})
		},

		async addRoom() {
			try {
				console.log("Enter add room");
				this.addedFriends = [];
				await this.getListOfFriendsForCreateRoom();
				this.showPopUpCreateRoom = true;
			} catch (exception) {
				console.log("Error in addRoom ", exception);
			}
		},
		async confirmCreateRoom() {
			try {
				if (!this.checkValidForCreateRoom) return;
				console.log("zo");
				const formData = new FormData();
				formData.append('groupName', this.groupName);
				formData.append('groupAvatarFile', this.groupAvatarFile);
				const receiversPhoneNumber = this.addedFriends.map(friend => friend.phoneNumber);
				console.log(receiversPhoneNumber);
				formData.append('receiversPhoneNumber', JSON.stringify(receiversPhoneNumber));
				try {
					const result = await axios.post(`http://localhost:8181/v1/chat/create-room`, formData, {
						headers: {
							'Content-Type': 'multipart/form-data'
						}
					}).catch(error => {
						console.log(error);
					});
					if (result.status === 200) {
						console.log("zo tiep");
						this.toast.success(result.data, { timeout: 1500 });
						this.closeCreateRoomDialog();
						this.fetchMoreRooms();
					} else {
						this.toast.error(result.data, { timeout: 1500 });
					}
					// const room 
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
			} catch (exception) {
				console.log("Error in confirmCreateRoom ", exception);
			}
		},
		closeCreateRoomDialog() {
			try {
				this.showPopUpCreateRoom = false;
				this.addedFriends = [];
				this.groupAvatarFile = null;
				this.groupName = "";
			} catch (exception) {
				console.log("Error in closeCreateRoomDialog ", exception);
			}
		},

		getFileGroupAvatar() {
			try {
				console.log("Gọi hàm: openFilePicker");
				const fileInput = this.$refs.groupAvatarFile;
				if (fileInput) {
					fileInput.click();
				}
			} catch (exception) {
				console.log("Error in getFileGroupAvatar ", exception);
			}
		},
		handleFileChange() {
			try {
				console.log("Gọi hàm: handleFileChange");
				const selectedFile = event.target.files[0];
				this.groupAvatarFile = selectedFile;
			} catch (exception) {
				console.log("Error in handleFileChange ", exception);
			}
		},
		getUrl(file) {
			try {
				console.log("Gọi hàm: getUrl");
				return URL.createObjectURL(file);
			} catch (error) {
				console.log("Hàm getUrl lỗi rồi");
			}
		},
		async callApiUpdateMessage(roomId, message) {
			try {
				console.log('Call api update message function');
				console.log(message);
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
				// console.log("Form");
				// console.log(form.get('replyMessageId'));
				const files = message.files;
				if (files) {
					// console.log("Test file");
					for (const file of files) {
						// console.log("File");
						// console.log(file);
						const blob = !file.blob ? await fetch(file.url).then(r => r.blob()) : [];
						// console.log(blob);
						const convertFile = new File([file.blob ? file.blob : blob], file.type === 'audio/mp3' ? file.name : (file.extension ? file.name.concat('.').concat(file.extension) : file.name.concat('.').concat(file.type)), {
							type: file.type
						});
						// console.log("Convert file");
						// console.log(convertFile);
						form.append('files', convertFile);
					}
				} else {
					form.append('files', null);
				}

				// console.log("File form");
				// console.log(form.get('files'));
				const result = await axios.put(`http://localhost:8181/v1/chat/update-message`, form, {
					headers: {
						'Content-Type': 'multipart/form-data'
					}
				}).catch(error => {
					console.log(error);
				});
				// console.log(result.data);
				return result;
			} catch (exception) {
				console.log("Error in callApiUpdateMessage ", exception);
			}
		},

		async sendMessage({ roomId, content, files, replyMessage, usersTag }) {
			// console.log("Call send message");
			// console.log("RoomId: ", roomId);
			// console.log("Content: ", content);
			// console.log("Files: ", files);
			// console.log("ReplyMessage: ", replyMessage);
			// console.log("User tag:", usersTag);
			try {
				const MAX_FILE_SIZE = 100 * 1024 * 1024;
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
					console.log("Files");
					console.log(files);
					for (const file of files) {
						if (!file.blob.type.includes("video") && !file.blob.type.includes("image")) {
							alert(`Chỉ hỗ trợ định dạng ảnh và video`);
						} else if (file.size > MAX_FILE_SIZE) {
							alert(`File "${file.name}" không được hỗ trợ do có kích thước lớn hơn 100MB!`);
						} else {
							const convertFile = new File([file.blob], file.type === 'audio/mp3' ? file.name : (file.extension ? file.name.concat('.').concat(file.extension) : file.name.concat('.').concat(file.type)), {
								type: file.type
							});
							form.append('files', convertFile);
						}
					}
				}
				if (replyMessage)
					form.append('replyMessageId', replyMessage ? parseInt(replyMessage._id) : null);
				// console.log("Form");
				// console.log(form.get('files'));
				try {
					const result = await axios.post(`http://localhost:8181/v1/chat/create-message`, form, {
						headers: {
							'Content-Type': 'multipart/form-data'
						}
					}).catch(error => {
						console.log(error);
					});
					if (result.status === 200) {
						// console.log(result);
						result.data.chatMessageResponses.forEach(chatMessageResponse => {
							chatMessageResponse.saved = true;
							chatMessageResponse.distributed = true;
							if (files) {
								chatMessageResponse.files.forEach(file => {
									delete file.progress;
								});
							}
							this.messages = [...this.messages, chatMessageResponse];
						});
					} else {
						result.data.chatMessageResponses.forEach(chatMessageResponse => {
							chatMessageResponse.failure = true;
						});
					}
					// const room 
				} catch (exception) {
					// console.log("Loi roi");
					// console.log(exception);
				}
			} catch (exception) {
				console.log("Error in sendMessage ", exception);
			}
		},

		async sendMessageReaction({ roomId, messageId, reaction, remove }) {
			try {
				// console.log("Call send message reaction");
				// console.log("Room Id: ", roomId);
				// console.log("Message Id: ", messageId);
				// console.log("Reaction: ", reaction);
				// console.log("Remove: ", remove);
				const message = this.messages.find(message => message._id == messageId);
				// console.log(message);
				if (!message) return;
				const reactionUser = Object.prototype.hasOwnProperty.call(message.reactions, reaction.unicode) ? message.reactions[reaction.unicode] : [];
				// console.log("Reaction");
				// console.log(reactionUser);

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
						// console.log("Error");
						message.reactions = oldReactions;
					}
				} catch (error) {
					// console.log(error);
				}
			} catch (exception) {
				console.log("Error in sendMessageReaction ", exception);
			}
		},

		async deleteMessage({ roomId, message }) {
			// console.log("Call delete message");
			// console.log("RoomId:", roomId)
			// console.log("Message: ", message);
			try {
				const result = await axios.delete(`http://localhost:8181/v1/chat/delete-message/${message._id}`).catch(function (error) {
					// console.log(error);
				})
				if (result.status === 200) {
					// console.log("Set message deleted");
					// message.deleted = true;
					// console.log(this.messages);
					const currentMessage = this.messages.find(currentMessage => currentMessage._id == message._id);
					currentMessage.deleted = true;
				}
			} catch (exception) {
				// console.log(exception);
			}
		},
		async showRoomInfo(room) {
			console.log("Call show room info");
			console.log(room);
			try {
				const result = await axios.get(`http://localhost:8181/v1/chat/get-room-info/${room.roomId}`).catch(error => {
					console.log(error);
				});
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
				}).catch(error => {
					console.log(error);
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
			try {
				if (this.showPopUpInfoRoomWithMembers) {
					if (this.showPopUpInfoRoomWith2Members) {
						this.showPopUpInfoRoomWith2Members = false;
					} else {
						this.showPopUpInfoRoomWithMembers = false;
						this.showPopUpInfoRoomWith2Members = false;
					}
				} else {
					this.showPopUpInfoRoomWith2Members = false;
					this.showPopUpInfoRoomWithMembers = false;
				}
			} catch (exception) {
				console.log("Error in closePopupInfoRoom ", exception);
			}
		},

		roomActionHandler({ roomId, action }) {
			// console.log("Call roomActionHandler function");
			// console.log("RoomId", roomId);
			// console.log("Action", action);
		},

		async menuActionHandler({ roomId, action }) {
			try {
				// console.log("Call menuActionHandler function");
				// console.log("RoomId", roomId);
				// console.log("Action", action);
				const room = this.rooms.find(room => room.roomId === roomId);
				const toPhoneNumberUser = room.users.find(user => user.phoneNumber != this.user.phoneNumber).phoneNumber;
				if (action.name === 'callUser') {
					this.videoCallDialog = true;
					await this.callToSpecificUser(room);
				} else if (action.name === 'unblockUser') {
					// console.log("ToPhoneNumberUser");
					// console.log(toPhoneNumberUser);
					await this.unBlockUser(toPhoneNumberUser);
				} else if (action.name === 'blockUser') {
					await this.blockUser(toPhoneNumberUser);
				} else if (action.name === 'unFriendUser') {
					await this.unFriend(toPhoneNumberUser);
				} else if (action.name === 'sendInviteFriend') {
					await this.addFriend(toPhoneNumberUser);
				}
				if (action.name === 'quitRoom') {
					this.quitRoom(room);
				} if (action.name === 'inviteUser') {
					this.inviteUser(room);
				}
			} catch (exception) {
				console.log("Error in menuActionHandler ", exception);
			}
		},
		async showUserInfoDialog(friend) {
			try {
				console.log(friend);
				await this.getUserInfo(friend._id);

				this.formattedBirthday();

				this.showPopUpInfoRoomWith2Members = true;
			} catch (exception) {
				console.log("Error in menuActionHandler ", exception);
			}
		},
		async getListOfFriends(room) {
			console.log("Gọi hàm: getListOfFriends");
			try {
				const response = await axios.get(`users/getListFriendsAreNotInRoom/${room.roomId}`, {
					headers: {
						'Authorization': localStorage.getItem("token")
					}
				}).catch(error => {
					console.log(error);
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
		async getListOfFriendsForCreateRoom() {
			try {
				const response = await axios.get(`users/getAllFriendUser`, {
					headers: {
						'Authorization': localStorage.getItem("token")
					}
				}).catch(error => {
					console.log(error);
				});

				if (response.status === 200) {

					this.listFriendsForCreateRoom = response.data;
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
			try {
				console.log("Gọi hàm: deleteFriendByPhoneNumber");
				if (this.showPopUpInviteUserToRoom) {
					this.listFriends = this.listFriends.filter(friend => friend.phoneNumber !== phoneNumber);
				} else if (this.showPopUpCreateRoom) {
					this.listFriendsForCreateRoom = this.listFriendsForCreateRoom.filter(friend => friend.phoneNumber !== phoneNumber);
				}
			} catch (exception) {
				console.log("Error deleteFriendByPhoneNumber ", exception);
			}

		},
		addFriendTag(friend) {
			try {
				this.addedFriends.push(friend);
				this.deleteFriendByPhoneNumber(friend.phoneNumber);
			} catch (exception) {
				console.log("Error in addFriendTag ", exception);
			}
		},
		deleteFriendTag(friend) {
			try {
				const indexInTag = this.addedFriends.findIndex(taggedFriend => taggedFriend.phoneNumber === friend.phoneNumber);
				if (indexInTag !== -1) {
					this.addedFriends.splice(indexInTag, 1);
				}

				if (this.showPopUpInviteUserToRoom) {
					const indexInFriends = this.listFriends.findIndex(existingFriend => existingFriend.phoneNumber === friend.phoneNumber);
					if (indexInFriends === -1) {
						this.listFriends.push(friend);
					}
				} else if (this.showPopUpCreateRoom) {
					const indexInFriends = this.listFriendsForCreateRoom.findIndex(existingFriend => existingFriend.phoneNumber === friend.phoneNumber);
					if (indexInFriends === -1) {
						this.listFriendsForCreateRoom.push(friend);
					}
				}
			} catch (exception) {
				console.log("Error in deleteFriendTag ", exception);
			}

		},
		async inviteUser(room) {
			try {
				console.log(room);
				await this.getListOfFriends(room);
				this.addedFriends = [];
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
				const result = await axios.post(`http://localhost:8181/v1/chat/add-users-to-room/${this.currentRoomInfo.roomId}`, usersPhoneNumber).catch(error => {
					console.log(error);
				});
				if (result.status === 200) {
					console.log(result);
					this.toast.success("Thêm người dùng vào nhóm thành công!", { timeout: 1500 });
					this.closeChooseFriendDialog();
					// this.fetchMoreRooms();
				} else {
					this.toast.error(result.data, { timeout: 1500 });
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
		closeChooseFriendDialog() {
			try {
				this.showPopUpInviteUserToRoom = false;
				this.addedFriends = [];
			} catch (exception) {
				console.log("Error in closeChooseFriendDialog ", exception);
			}
		},

		async quitRoom(room) {
			try {
				if (confirm("Bạn có chắc muốn rời nhóm?")) {
					console.log(room);
					try {
						const result = await axios.delete(`http://localhost:8181/v1/chat/delete-room/${room.roomId}`).catch(error => {
							console.log(error);
						});
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
			} catch (exception) {
				console.log("Error in quitRoom ", exception);
			}
		},

		getCurrentUserId() {
			try {
				const user = JSON.parse(localStorage.getItem('user'));
				this.currentUser = user;
				this.currentUserId = user.id;
				// console.log(this.currentUserId);
			} catch (exception) {
				console.log("Error in getCurrentUserId ", exception);
			}
		},

		async fetchNewInfoRoom(roomId) {
			// console.log("Call fetchNewInfoRoom " + roomId);
		},

		async fetchNewInfoMessage(roomId) {
			// console.log("Call fetchNewInfoMessage" + roomId);
		},

		startTitleBlinking(newTitle) {
			try {
				if (isBlinking) return;
				isBlinking = true;
				blinkInterval = setInterval(() => {
					if (document.title === originalTitle) {
						document.title = newTitle;
					} else {
						document.title = originalTitle;
					}
				}, 1000); // Thay đổi mỗi giây
			} catch (exception) {
				console.log("Error in startTitleBlinking ", exception);
			}
		},

		stopTitleBlinking() {
			try {
				clearInterval(blinkInterval);
				document.title = originalTitle;
				isBlinking = false;
			} catch (exception) {
				console.log("Error in stopTitleBlinking ", exception);
			}
		},

		async handleNewUpdate(message) {
			try {
				console.log("zo test")
				const notification = JSON.parse(message.body);
				const userSend = message.headers.userSend;
				console.log(notification);
				try {
					if (notification.typeNotification === "RTC_CONNECTION") {
						if (notification.message) {
							if (notification.message.event === 'offer') {
								// if (this.RTCConnectionList.indexOf(userSend) === -1)
								await this.initializeRTCPeerConnection(userSend);
								await this.handleOffer(notification.message.data, userSend);
							} else if (notification.message.event === 'answer') {
								await this.handleAnswer(notification.message.data);
							} else if (notification.message.event === 'candidate') {
								await this.handleCandidate(notification.message.data);
							}
						}
					} else {
						if (parseInt(this.currentRoomId) === parseInt(notification.roomId)) {
							console.log("Inside");
							if (notification?.message?.files) {
								notification.message.files.forEach(file => {
									delete file.progress;
								});
							}
							if (notification.typeNotification === "CREATE") {
								console.log("zo")
								console.log("Notification");
								console.log(notification.roomInfo.roomId);
								console.log("Current User Id");
								console.log(this.currentUserId);
								if (parseInt(this.currentUserId) !== parseInt(notification.message.senderId) && !notification.message.system) {
									this.playSound();
									if (parseInt(this.currentRoomId) === parseInt(notification.roomInfo.roomId)) {
										this.messages = [...this.messages, notification.message];
										this.startTitleBlinking(notification.message.username + " đã gửi tin nhắn cho bạn");

									}
								} else if (notification.message.system){
									this.messages = [...this.messages, notification.message];
								}
							} else if (notification.typeNotification === "UPDATE") {
								// console.log("Message is update");
								if (notification.message === "Người dùng từ chối nhận cuộc gọi!") {
									this.callDeclined = true;
									this.toast.info("Người dùng từ chối nhận cuộc gọi!", { timeout: 1500 });
								} else if (notification.message === "Người dùng đã kết thúc cuộc gọi!") {
									this.closeDialog();
									this.toast.info("Đối phương đã kết thúc cuộc gọi!", { timeout: 1500 });
								} else {
									let message = this.messages.find(message => message._id == notification.message._id);
									// console.log(message);
									if (message) {
										const indexMessage = this.messages.indexOf(message);
										this.messages[indexMessage] = notification.message;
										this.messages = [...this.messages];
									}
								}
							} else if (notification.typeNotification === "BLOCK") {
								if (parseInt(this.currentRoomId) === parseInt(notification.roomInfo.roomId)) {
									this.messages = [...this.messages, notification.message];
									const menuActionList = [
										{ name: 'unblockUser', title: 'Unblock' },
										// { name: 'deleteRoom', title: 'Delete Room' },
									]
									this.menuActions = menuActionList;
								}
							} else if (notification.typeNotification === "UNBLOCK") {
								const menuActionList = [
									// { name: 'sendInviteFriend', title: 'Send Invite Friend' },
									// { name: 'deleteRoom', title: 'Delete Room' },
								]
								this.menuActions = menuActionList;
								this.messages[this.messages.length-1].content = 'Không thể tiếp tục gửi tin nhắn do hai người chưa là bạn bè';
							} else if (notification.typeNotification === "FRIEND") {
								//
								const menuActionList = [
									{ name: 'callUser', title: 'Call' },
									{ name: 'blockUser', title: 'Block' },
									// { name: 'unFriendUser', title: 'Unfriend' }
								];
								this.menuActions = menuActionList;
							} else if (notification.typeNotification === "UNFRIEND") {
								//
								const menuActionList = [
									{ name: 'sendInviteFriend', title: 'Send Invite Friend' },
									// { name: 'deleteRoom', title: 'Delete Room' },
								]
								this.menuActions = menuActionList;
								this.messages[this.messages.length-1].content = 'Không thể tiếp tục gửi tin nhắn do hai người chưa là bạn bè';
							}
						} else {
							if (notification.typeNotification === "CREATE") {
								console.log("Notification")
								console.log(notification)
								console.log("zo")
								if (parseInt(this.currentUserId) !== parseInt(notification.message.senderId)) {
									this.playSound();
									this.startTitleBlinking(notification.message.username + " đã gửi tin nhắn cho bạn");
								}
								const checkShowFooter = this.messages.some(message => message.system && message.content.includes('Không thể tiếp tục gửi tin nhắn'));
								this.showFooter = !checkShowFooter;
							}
						}
						console.log(this.rooms);
					}
					const checkShowFooter = this.messages.some(message => message.system && message.content.includes('Không thể tiếp tục gửi tin nhắn'));
					this.showFooter = !checkShowFooter;
					const indexRoom = this.rooms.indexOf(this.rooms.find(room => room.roomId == notification.roomInfo.roomId));
					if (indexRoom !== -1) {
						this.rooms[indexRoom] = notification.roomInfo;
						this.rooms = [...this.rooms];
					} else {
						const newInfoRooms = this.rooms;
						newInfoRooms.push(notification.roomInfo);
						this.rooms = newInfoRooms;
					}
				} catch (exception) {
					console.log(exception);
				}
			} catch (exception) {
				console.log("Error in handleNewUpdate ", exception);
			}
		},

		clearTimeouts() {
			try {
				if (this.timeoutId) {
					clearTimeout(this.timeoutId);
					this.timeoutId = null;
				}
			} catch (exception) {
				console.log("Error in clearTimeouts ", exception);
			}
		},

		async createMissedCallMessage() {
			try {
				const user = this.roomForNotification.users.find(element => element._id != this.currentUserId);
				if (user) {
					const messageContent = `${user.username} đã bỏ lỡ cuộc gọi!`;
					const form = new FormData();

					form.append('roomId', this.roomForNotification.roomId);
					form.append('content', messageContent);
					form.append('system', true);
					form.append('saved', false);
					form.append('distributed', false);
					form.append('seen', false);
					form.append('failure', false);
					form.append('disableActions', false);

					try {
						const result = await axios.post(`http://localhost:8181/v1/chat/create-message`, form, {
							headers: {
								'Content-Type': 'multipart/form-data'
							}
						}).catch(error => {
							console.log(error);
						});
						if (result.status === 200) {
							result.data.chatMessageResponses.forEach(chatMessageResponse => {
								chatMessageResponse.saved = true;
								chatMessageResponse.distributed = true;
								this.messages = [...this.messages, chatMessageResponse];
							});
						} else {
							result.data.chatMessageResponses.forEach(chatMessageResponse => {
								chatMessageResponse.failure = true;
							});
						}
					} catch (exception) {
						console.log('Error creating message:', exception);
					}

					this.videoCallDialog = false;
				}
			} catch (exception) {
				console.log("Error in createMissedCallMessage ", exception);
			}
		},

		// --------------Config web socket--------------

		async callToSpecificUser(room) {
			console.log("Call function callToSpecificUser");
			// console.log("Room info");
			// console.log(room);
			try {
				const user = room.users.filter(element => element._id != this.currentUserId);
				this.roomForNotification = room;
				await this.initializeRTCPeerConnection(this.currentUserId);
				const stream = await this.getStream();
				this.localStream = stream;
				stream.getTracks().forEach((track) => {
					this.peerConnection.addTrack(track, stream);
				});
				// const timeout = setTimeout(await this.createOffer(), 10000, user[0]._id);
				await this.createOffer(user[0]._id);
			} catch (exception) {
				console.log(exception);
			}
			// this.dataChannel.send("Test Web RTC");
		},

		async subscribeSpecificUserWebSocket() {
			try {
				this.socket = new SockJS('http://localhost:8181/room');
				this.stompClient = Stomp.over(this.socket);

				// var sessionId = "";
				var userId = JSON.parse(localStorage.getItem('user'))['id'];

				await this.stompClient.connect({ userId: 'user' + userId }, frame => {
					// console.log("Frame");
					// console.log(frame);
					if (this.stompClient.connected) {
						// console.log("Connected success to server");
						this.stompClient.subscribe('/user/topic/specific-user', this.handleNewUpdate);
					}
					// console.log(this.stompClient.connected);
					// this.stompClient.send("/app/room", "Hehehe", { userId: 'user' + 2 });
				}, this.handleErrorSubscribe);
				this.stompClient.onMessage = message => this.handleNewUpdate(message);
			} catch (exception) {
				console.log("Error in subscribeSpecificUserWebSocket ", exception);
			}
		},

		async initializeRTCPeerConnection(userId) {
			try {
				// console.log("Call initializeRTCPeerConnection");
				var configuration = {
					iceServers: [
						{
							urls: 'stun:stun.l.google.com:19302'
						}
					]
				};

				this.peerConnection = new RTCPeerConnection(configuration);

				// Setup ice handling
				this.peerConnection.onicecandidate = async event => {
					console.log("Call send candidate");
					if (event.candidate) {
						await this.send(JSON.stringify({
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

				// this.dataChannel.onopen = function () {

				// };

				this.dataChannel.onclose = function () {
					console.log("data channel is closed");
				};

				this.peerConnection.ondatachannel = function (event) {
					this.dataChannel = event.channel;
					// when we receive a message from the other peer, printing it on the console
					this.dataChannel.onmessage = function (event) {
						console.log("message:", event.data);
					};
				};
				this.peerConnection.ontrack = ev => {
					console.log("On track functin fire");
					if (ev.streams && ev.streams[0]) {
						this.callAccepted = true;
						this.remoteStream = ev.streams[0];
					}
				};
			} catch (exception) {
				console.log("Error in initializeRTCPeerConnection ", exception);
			}
		},

		async createOffer(userId) {
			try {
				const offer = await this.peerConnection.createOffer();
				await this.peerConnection.setLocalDescription(offer);
				await this.send(JSON.stringify({
					event: "offer",
					data: offer
				}), userId);

				this.timeout = setTimeout(this.createMissedCallMessage, 10000);
			} catch (error) {
				console.log("Error creating an offer");
			}
		},

		async handleOffer(offer, remoteUser) {
			try {
				console.log("Handle")
				this.callIncoming = true;
				this.callAccepted = false;
				this.callDeclined = false;
				this.offerData = offer;
				this.remoteUser = remoteUser;
				this.confirmModal = Modal.confirm({
					title: 'Incoming Call',
					content: 'You have an incoming call. Do you want to accept it?',
					onOk: () => {
						this.acceptCall();
					},
					onCancel: () => {
						this.declineCall();
					}
				});
				this.timeout = setTimeout(() => {
					this.closeForm();
					if (this.confirmModal) {
						this.confirmModal.destroy();
					}
				}, 10000);
			} catch (exception) {
				console.log("Error in handleOffer ", exception);
			}
		},

		closeForm() {
			try {
				this.callAccepted = false;
				this.callDeclined = false;
				this.callIncoming = false;
				this.offerData = null;
				this.remoteUser = null;
			} catch (exception) {
				console.log("Error in closeForm ", exception);
			}
		},

		async handleCandidate(candidate) {
			try {
				if (candidate) {
					await this.peerConnection.addIceCandidate(new RTCIceCandidate(candidate)).catch(e => {
						console.log(e);
					})
				}
			} catch (exception) {
				console.log(exception);
			}
		},

		async handleAnswer(answer, remoteUser) {
			try {
				this.peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
				// console.log("connection established successfully!!");
				this.RTCConnectionList.push(remoteUser);
				// console.log(this.RTCConnectionList);
			} catch (exception) {
				console.log(exception);
			}
		},

		async send(object, userId) {
			try {
				// console.log("Call send function", object);
				this.stompClient.send("/app/room", object, { userId: 'user' + userId, userSend: 'user' + this.currentUserId });
			} catch (exception) {
				console.log("Error in send ", exception);
			}
		},

		sendMessageRTC() {
			try {
				this.dataChannel.send("Test");
			} catch (exception) {
				console.log("Error in sendMessageRTC ", exception);
			}
		},

		handleErrorSubscribe() {
			// console.log("Have error when subscribe websocket");
		},

		async getStream() {
			try {
				const stream = await navigator.mediaDevices.getUserMedia(this.constraints, (stream) => {
					stream.getTracks().forEach((track) => {
						this.peerConnection.addTrack(track, stream);
					});
				});
				return stream;
			} catch (error) {
				console.error('Error getting user media:', error);
				throw error; // Throwing the error to handle it in the caller function
			}
		},

		async acceptCall() {
			try {
				this.callIncoming = false;
				this.callAccepted = true;
				this.videoCallDialog = true;
				await this.peerConnection.setRemoteDescription(new RTCSessionDescription(this.offerData));
				const stream = await this.getStream();
				this.localStream = stream;
				stream.getTracks().forEach((track) => {
					this.peerConnection.addTrack(track, stream);
				});

				const answer = await this.peerConnection.createAnswer();
				await this.peerConnection.setLocalDescription(answer);
				await this.send(JSON.stringify({
					event: "answer",
					data: answer
				}), this.remoteUser);
			} catch (exception) {
				console.log(exception);
			}
		},
		async declineCall() {
			try {
				this.callIncoming = false;
				this.offerData = null;
				this.remoteUser = null;
				this.callDeclined = true;
				const room = this.rooms.filter(element => element.roomId === this.currentRoomId)[0];
				const user = room.users.filter(element => element._id !== this.currentUserId)[0];
				const receiverId = user._id;
				const form = new FormData();
				form.append('roomId', this.currentRoomId);
				form.append('receiverId', receiverId);
				form.append('message', "Người dùng từ chối nhận cuộc gọi!");
				const result = await axios.post(`http://localhost:8181/v1/chat/send-notification-declined`, form).catch(error => {
					console.log(error);
				});
				// console.log(result.data);
				return result;
				// You may want to send a message back to the caller indicating the call was declined
			} catch (exception) {
				console.log("Error in declineCall ", exception);
			}
		},

		// --------------End config websocket--------------

		handleUserChangeTab() {
			return function () {
				window.onfocus = window.onblur = window.onpageshow = window.onpagehide = function (e) {
					if ({ focus: 1, pageshow: 1 }[e.type]) {
						// console.log("User is in current tab");
					} else {
						// console.log("User is not in current tab");
					}
				};
			};
		},
		getMessageStyle(message) {
			try {
				if (message.content.includes('đã bỏ lỡ cuộc gọi!')) {
					return { color: 'red' };
				}
				return {};
			} catch (exception) {
				console.log("Error in getMessageStyle ", exception);
			}
		},
		async endCall() {
			try {
				console.log("Đã gọi endcall!");
				const room = this.rooms.filter(element => element.roomId === this.currentRoomId)[0];
				const otherUser = room.users.find(user => user._id != this.currentUser.id);
				console.log(otherUser._id);
				const form = new FormData();
				form.append('roomId', this.currentRoomId);
				form.append('receiverId', otherUser._id);
				form.append('message', "Người dùng đã kết thúc cuộc gọi!");
				const result = await axios.post(`http://localhost:8181/v1/chat/send-notification-declined`, form).catch(error => {
					console.log(error);
				});
				// console.log(result.data);
				this.closeDialog();
			} catch (exception) {
				console.log("Error in endCall ", exception);
			}
		},

		async openFileHandler({ message, file }) {
			try {
				console.log("Call openFileActionHandler");
				console.log("Message");
				console.log(message);
				console.log("File");
				console.log(file);
				console.log(file.file.url);
				window.open(file.file.url, '_blank');
			} catch (exception) {
				console.log("Error in menuActionHandler ", exception);
			}
		},

		async blockUser(phoneNumber) {
			try {
				const friendRequest = {
					fromPhoneNumberUser: this.user.phoneNumber,
					toPhoneNumberUser: phoneNumber,
				}

				const response = await axios.post(`users/blockFriendUser`, friendRequest, {
					headers: {
						'Authorization': localStorage.getItem("token")
					}
				});

				if (response.status === 200) {
					// await this.getListOfFriends();
					this.toast.success(response.data, { timeout: 1500 });
				} else {
					console.error(response.body);
					this.toast.error(response.data || 'Đã xảy ra lỗi!', { timeout: 1500 });
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

		async unBlockUser(phoneNumber) {
			try {
				const friendRequest = {
					fromPhoneNumberUser: this.user.phoneNumber,
					toPhoneNumberUser: phoneNumber,
				}

				const response = await axios.post(`users/unBlockFriendUser`, friendRequest, {
					headers: {
						'Authorization': localStorage.getItem("token")
					}
				});

				if (response.status === 200) {
					// this.friends = this.friends.filter(friend => friend.phoneNumber !== phoneNumber);
					this.toast.success(response.data, { timeout: 1500 });
				} else {
					console.error(response.body);
					this.toast.error(response.data || 'Đã xảy ra lỗi!', { timeout: 1500 });
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

		async unFriend(phoneNumber) {
			try {
				const friendRequest = {
					fromPhoneNumberUser: this.user.phoneNumber,
					toPhoneNumberUser: phoneNumber,
				}

				const response = await axios.post(`users/unFriendUser`, friendRequest, {
					headers: {
						'Authorization': localStorage.getItem("token")
					}
				});

				if (response.status === 200) {
					this.toast.success(response.data, { timeout: 1500 });
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
		async addFriend(toPhoneNumberUser) {
			try {

				const userString = localStorage.getItem('user');
				if (userString) {
					const sender = JSON.parse(userString);

					const addFriendRequest = {
						fromPhoneNumberUser: sender.phoneNumber,
						toPhoneNumberUser: toPhoneNumberUser,
						isAcceptingInvite: false
					}

					console.log(addFriendRequest)

					const responseUser = await axios.post(`users/sendInviteFriend`, addFriendRequest, {
						headers: {
							'Authorization': localStorage.getItem("token")
						}
					});

					if (responseUser.status === 200) {
						this.toast.success(responseUser.data, { timeout: 1500 })
						this.showVisibleUserInfo = false;
						this.searchPhoneNumber = '';
						this.showVisibleFindFriendDialog = true;
					} else {
						console.error(responseUser.data);
						this.toast.error(responseUser.data || 'Đã xảy ra lỗi!', { timeout: 1500 });
					}
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

			.create_group_header {
				height: 80px;
				padding-top: 0;
				margin-bottom: -20px;
				padding: 16px 16px 0;
				display: flex;

				.create_group_avatar {
					width: 46px;
					height: 46px;
					border-radius: 50%;
					border: 1px solid #d6dbe1;
					font-size: 1.5rem;
					font-weight: 400;
					line-height: 1.5;
					color: #7589a3;
					overflow: hidden;
					justify-content: center;
					align-items: center;
				}

				.create_group_input {
					margin-left: 10px;
					flex-grow: 1;
					padding-top: 4px;

					.span_input {
						border-top: none;
						border-left: none;
						border-right: none;
						border-radius: 0;
						height: 38px;
						line-height: 38px;
						display: flex;
						overflow: hidden;
						background: #fff;
						position: relative;
						width: 100%;
						border: 1px solid #d6dbe1;
						box-sizing: border-box;

						.input_name {
							padding: 0;
							box-sizing: border-box;
							list-style: none;
							position: relative;
							width: 100%;
							color: #081c36;
							background-image: none;
							border: none;
							outline: none;
							border-radius: 4px;
							transition: all .3s;
							font-size: .875rem;
							font-weight: 400;
							line-height: 1.5;
							background: transparent;
							padding-left: 8px;
						}
					}
				}
			}

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

.disabled {
	pointer-events: none;
	opacity: 0.5;
}

.custom-dialog .v-card {
	height: 90vh;
	/* 90% of the viewport height */
}
</style>