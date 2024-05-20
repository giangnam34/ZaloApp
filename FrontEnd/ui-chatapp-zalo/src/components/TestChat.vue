<template>
	<div>
		<vue-advanced-chat height="calc(100vh - 20px)" :current-user-id="currentUserId" :rooms="JSON.stringify(rooms)"
			:loading-rooms="loadingRooms" @fetch-more-rooms="fetchMoreRooms" :rooms-loaded="roomsLoaded"
			:room-actions="JSON.stringify(roomActions)" :menu-actions="JSON.stringify(menuActions)"
			:messages="JSON.stringify(messages)" :messages-loaded="messagesLoaded" :load-first-room="loadFirstRoom"
			:show-footer="showFooter" @send-message="sendMessage($event.detail[0])"
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

		sendMessage(message) {
			console.log("Call send message");
			console.log(message);
			// this.messages = [
			// 	...this.messages,
			// 	{
			// 		_id: this.messages.length,
			// 		content: message.content,
			// 		senderId: this.currentUserId,
			// 		timestamp: new Date().toString().substring(16, 21),
			// 		date: new Date().toDateString()
			// 	}
			// ]

		},

		sendMessageReaction({ roomId, messageId, reaction, remove }) {
			console.log("Call send message reaction");
			console.log("Room Id: ", roomId);
			console.log("Message Id: ", messageId);
			console.log("Reaction: ", reaction);
			console.log("Remove: ", remove);
			const message = this.messages.find(message => message._id == messageId);
			console.log(message);
			const reactionUser = Object.prototype.hasOwnProperty.call(message.reactions,reaction.unicode) ? message.reactions[reaction.unicode] : [];
			console.log(reactionUser);
			if (!reactionUser.includes(this.currentUserId))
				reactionUser.push(this.currentUserId);
			else if (remove === false){
				const index = reactionUser.indexOf(this.currentUserId);
				reactionUser.splice(index,1);
			}
			message.reactions[reaction.unicode] = reactionUser;
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