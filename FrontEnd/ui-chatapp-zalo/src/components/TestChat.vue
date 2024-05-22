<template>
	<div>
		<vue-advanced-chat height="calc(100vh - 20px)" :current-user-id="currentUserId" :rooms="JSON.stringify(rooms)"
			:loading-rooms="loadingRooms" @fetch-more-rooms="fetchMoreRooms" :rooms-loaded="roomsLoaded"
			:room-actions="JSON.stringify(roomActions)" :menu-actions="JSON.stringify(menuActions)"
			:messages="JSON.stringify(messages)" :messages-loaded="messagesLoaded" :load-first-room="loadFirstRoom"
			:show-footer="showFooter" @send-message="sendMessage($event.detail[0])" :room-info-enabled="roomInfo"
			@fetch-messages="fetchMessages($event.detail[0])" :templates-text="JSON.stringify(templatesText)"
			@send-message-reaction="sendMessageReaction($event.detail[0])" :theme="theme"
			:emoji-data-source="emojiDataSource" />
	</div>
</template>

<script>
import { register } from 'vue-advanced-chat'
import axios from "axios";
// import { register } from '../../vue-advanced-chat/dist/vue-advanced-chat.es.js'
register()

export default {
	data() {
		return {
			currentUserId: null,
			theme: 'light',
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
			emojiDataSource: "https://cdn.jsdelivr.net/npm/emoji-picker-element-data@%5E1/en/emojibase/data.json"
		}
	},

	created() {
		this.getCurrentUserId();
		this.fetchMoreRooms();
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
					page: this.messagePage++
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
			if (result.data.totalPages === result.data.currentPage + 1) {
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

		async callApiUpdateMessage(roomId, message) {
			// const form = new FormData();
			// form.append('roomId', 1);
			console.log("Message reactions: ");
			console.log(message.reactions);
			const result = await axios.put(`http://localhost:8181/v1/chat/update-message`, {
				'roomId': roomId,
				'messageId': message._id,
				'content': message.content,
				'system': message.system,
				'saved': message.saved,
				'distributed': message.distributed,
				'seen': message.seen,
				'failure': message.failure,
				'disableActions': message.disableActions,
				'disableReactions': message.disableReactions,
				'files': message.files,
				'reactions': JSON.stringify(message.reactions),
				'replyMessageId': message.replyMessage,
			}, {
				headers: {
					'Content-Type': 'multipart/form-data'
				}
			});
			return result;
		},
		async sendMessage({ roomId, content, files, replyMessage, usersTag }) {
			console.log("Call send message");
			console.log("RoomId: ", roomId);
			console.log("Content: ", content);
			console.log("Files: ", files[0]);
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
			files.forEach(file => {
				const convertFile = new File([file.blob], file.name.concat('.').concat(file.extension),{
					type: file.type
				}
				);
				form.append('files', convertFile);
			});
			// form.append('replyMessageId', replyMessage ? parseInt(replyMessage._id) : null);
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
			const reactionUser = Object.prototype.hasOwnProperty.call(message.reactions, reaction.unicode) ? message.reactions[reaction.unicode] : [];
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

		getCurrentUserId() {
			const user = JSON.parse(localStorage.getItem('user'));
			this.currentUserId = user.id;
			console.log(this.currentUserId);
		}
	}
}
</script>

<style lang="scss">
body {
	font-family: 'Quicksand', sans-serif;
}
</style>