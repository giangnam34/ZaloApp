<template>
	<div>
		<vue-advanced-chat height="calc(100vh - 20px)" :current-user-id="currentUserId" :rooms="JSON.stringify(rooms)"
			:loading-rooms="loadingRooms" @fetch-more-rooms="fetchMoreRooms" :rooms-loaded="roomsLoaded"
			:room-actions="JSON.stringify(roomActions)" :menu-actions="JSON.stringify(menuActions)"
			:messages="JSON.stringify(messages)" :messages-loaded="messagesLoaded" :load-first-room="loadFirstRoom"
			:show-footer="showFooter" @send-message="sendMessage($event.detail[0])"
			@fetch-messages="fetchMessages($event.detail[0])"
			:templates-text="JSON.stringify(templatesText)" :theme="theme" :emoji-data-source="emojiDataSource" />
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
			currentUserId: '1234',
			theme: 'light',
			// rooms: [
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '2',
			// 		roomName: 'Room 2',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '3',
			// 		roomName: 'Room 3',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	},
			// 	{
			// 		roomId: '1',
			// 		roomName: 'Room 1',
			// 		avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
			// 		users: [
			// 			{ _id: '1234', username: 'John Doe' },
			// 			{ _id: '4321', username: 'John Snow' }
			// 		]
			// 	}
			// ],
			rooms: [],
			roomsLoaded: false,
			loadingRooms: true,
			loadFirstRoom: false,
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
		this.fetchMoreRooms();
	},

	methods: {
		async fetchMessages({ room = {}, options = {} }) {
			console.log("Option messages: ");
			console.log(options);
			console.log(room);
			this.messagesLoaded = false;
			try {
				if (options.reset) {
					console.log("Options is reset");
					const result = await axios.get('http://localhost/FakeApiChatApp/message.json');
					// console.log("Log first");
					console.log("Result: ");
					console.log(result.data);
					this.messages = [...result.data, ...this.messages];
					// this.messagesLoaded = true;
				} else {
					console.log("Options is not reset");
					// Not done
					this.messages = [...this.addMessages(), ...this.messages]
					this.messagesLoaded = true
				}
			} catch (err) {
				console.log(err.toString())
			}
			// console.log(this.messages)
		},

		async fetchMoreRooms() {
			// setTimeout(() => {
			// 	this.loadingRooms = true;
			// }, 1000);
			this.loadingRooms = true;
			console.log("Call fetchRooms");
			const rooms = this.rooms;
			const room = {
				roomId: '2',
				roomName: 'Room 2',
				avatar: 'https://66.media.tumblr.com/avatar_c6a8eae4303e_512.pnj',
				users: [
					{ _id: '1234', username: 'John Doe' },
					{ _id: '4321', username: 'John Snow' }
				]
			}
			rooms.push(room);
			this.rooms = rooms;
			this.loadingRooms = false;
			// this.roomsLoaded = true;
			// console.log(event);
			// this.rooms = [];
			// this.roomsLoaded = true;
			// try {
			// 	// if (options.reset) {
			// 	// 	console.log("Options is reset");
			// 	// 	const result = await axios.get('http://localhost/FakeApiChatApp/message.json');
			// 	// 	// console.log("Log first");
			// 	// 	console.log("Result: ");
			// 	// 	console.log(result.data);
			// 	// 	this.messages = [...result.data, ...this.messages];
			// 	// 	// this.messagesLoaded = true;
			// 	// } else {
			// 	// 	console.log("Options is not reset");
			// 	// 	// Not done
			// 	// 	this.messages = [...this.addMessages(), ...this.messages]
			// 	// 	this.messagesLoaded = true
			// 	// }
			// } catch (err) {
			// 	console.log(err.toString())
			// }
			// console.log(this.messages)
		},

		addMessages(reset) {
			const messages = []

			for (let i = 0; i < 30; i++) {
				messages.push({
					_id: reset ? i : this.messages.length + i,
					content: `${reset ? '' : 'paginated'} message ${i + 1}`,
					senderId: '4321',
					username: 'John Doe',
					date: '13 November',
					avatar: 'https://dfstudio-d420.kxcdn.com/wordpress/wp-content/uploads/2019/06/digital_camera_photo-1080x675.jpg',
					system: false,
					timestamp: '10:20'
				})
			}

			return messages
		},

		addNewMessage() {
			setTimeout(() => {
				this.messages = [
					...this.messages,
					{
						_id: this.messages.length,
						content: 'NEW MESSAGE',
						senderId: '1234',
						timestamp: new Date().toString().substring(16, 21),
						date: new Date().toDateString()
					}
				]
			}, 2000)
		},

		sendMessage(message) {
			this.messages = [
				...this.messages,
				{
					_id: this.messages.length,
					content: message.content,
					senderId: this.currentUserId,
					timestamp: new Date().toString().substring(16, 21),
					date: new Date().toDateString()
				}
			]

		},
	}
}
</script>

<style lang="scss">
body {
	font-family: 'Quicksand', sans-serif;
}
</style>