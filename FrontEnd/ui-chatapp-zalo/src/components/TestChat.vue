<template>
	<div>
		<vue-advanced-chat height="calc(100vh - 20px)" :current-user-id="currentUserId" :rooms="JSON.stringify(rooms)"
			:loading-rooms="loadingRooms" @fetch-more-rooms="fetchMoreRooms" :rooms-loaded="roomsLoaded"
			:room-actions="JSON.stringify(roomActions)" :menu-actions="JSON.stringify(menuActions)"
			:messages="JSON.stringify(messages)" :messages-loaded="messagesLoaded" :load-first-room="loadFirstRoom"
			:show-footer="showFooter" @send-message="sendMessage($event.detail[0])" :room-info-enabled="roomInfo"
			@fetch-messages="fetchMessages($event.detail[0])" :templates-text="JSON.stringify(templatesText)"
			@delete-message="deleteMessage($event.detail[0])"
			@send-message-reaction="sendMessageReaction($event.detail[0])" :theme="theme"
			@room-info="showRoomInfo($event.detail[0])" @edit-message="editMessage($event.detail[0])"
			:emoji-data-source="emojiDataSource" />
		<UserInfo v-model:showPopup="showPopUpInfoRoom"></UserInfo>

	</div>
</template>

<script>
import { register } from 'vue-advanced-chat'
import axios from "axios";
import UserInfo from './UserInfo.vue';
import SockJS from 'sockjs-client';
import Stomp from "webstomp-client";
// import { register } from '../../vue-advanced-chat/dist/vue-advanced-chat.es.js'
register()

export default {
	components: {
		UserInfo
	},
	data() {
		return {
			currentUserId: null,
			theme: 'light',
			currentRoom: null,
			rooms: [],
			roomsLoaded: false,
			loadingRooms: true,
			loadFirstRoom: true,
			roomInfo: true,
			roomActions: [
				{ name: 'inviteUser', title: 'Invite User' },
				{ name: 'removeUser', title: 'Remove User' },
				{ name: 'deleteRoom', title: 'Delete Room' },
			],
			menuActions: [
				{ name: 'inviteUser', title: 'Invite User' },
				{ name: 'removeUser', title: 'Remove User' },
				{ name: 'deleteRoom', title: 'Delete Room' },
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
			showPopUpInfoRoom: false,
			emojiDataSource: "https://cdn.jsdelivr.net/npm/emoji-picker-element-data@%5E1/en/emojibase/data.json"
		}
	},

	created() {
		this.getCurrentUserId();
		this.fetchMoreRooms();
		this.subcribeSpecificUserWebSocket();
		window.onfocus = this.handleUserChangeTab();
		window.onblur = this.handleUserChangeTab();
	},

	methods: {
		async fetchMessages({ room = {}, options = {} }) {
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
				const result = await this.callApiUpdateMessage(roomId, message);
				if (result.status === 200) {
					message.reactions[reaction.unicode] = reactionUser;
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

		showRoomInfo(room) {
			console.log("Call show room info");
			console.log(room);
			this.showPopUpInfoRoom = true;
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
			if (notification.typeNotification === "CREATE" && this.currentRoom == notification.roomId){
				this.messages = [...this.messages, notification.message];
			}
		},

		async subcribeSpecificUserWebSocket() {
			var socket = new SockJS('http://localhost:8181/room');
			var stompClient = Stomp.over(socket);
			// var sessionId = "";
			var userId = JSON.parse(localStorage.getItem('user'))['id'];
			console.log(userId);

			stompClient.connect({ userId: 'user' + userId }, frame => {
				console.log("Frame");
				console.log(frame);
				stompClient.subscribe('/user/topic/specific-user', this.handleNewUpdate);
				// stompClient.send("/app/room", "Hehehe", { userId: 'user' + 2 });
			}, this.handleErrorSubscribe);
			stompClient.onMessage = message => this.handleNewUpdate(message);
		},

		handleErrorSubscribe(){
			console.log("Have error when subscribe websocket");
		},
		
		handleUserChangeTab(){
			console.log("User is in current tab");
		}
	}
}
</script>

<style lang="scss">
body {
	font-family: 'Quicksand', sans-serif;
}
</style>