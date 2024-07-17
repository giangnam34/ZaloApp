<template>
    <h3 class="text-2xl font-bold text-left pb-[10px]">Quản lý bài viết</h3>
    <v-data-table :headers="headers" :items="posts" :sort-by="[{ key: 'id', order: 'asc' }]">
        <template v-slot:top>
            <v-dialog v-model="dialog" max-width="500px">
                <v-card>
                    <v-card-title>
                        <span class="text-h5">{{ formTitle }}</span>
                    </v-card-title>

                    <v-card-text>
                        <v-container>
                            <v-row>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.postUserName" label="Tên người đăng bài" readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.postUserPhoneNumber" label="Số điện thoại người đăng bài" readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.content" label="Nội dung bài viết" readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.audience" label="Chế độ hiển thị" readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.createdDate" label="Ngày tạo" readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.likesCount" label="Lượt thích" type="number" readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.commentsCount" label="Lượt bình luận" type="number" readonly></v-text-field>
                                </v-col>
                            </v-row>
                        </v-container>
                    </v-card-text>

                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="blue-darken-1" variant="text" @click="close">
                            Hủy
                        </v-btn>
                        <v-btn color="blue-darken-1" variant="text" @click="save">
                            Lưu
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
            <v-dialog v-model="dialogDelete" max-width="500px">
                <v-card>
                    <v-card-title class="text-h5 item-center">Confirm delete post?</v-card-title>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="blue-darken-1" variant="text" @click="closeDelete">Cancel</v-btn>
                        <v-btn color="blue-darken-1" variant="text" @click="deleteItemConfirm">Delete</v-btn>
                        <v-spacer></v-spacer>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </template>
        <template v-slot:[`item.actions`]="{ item }">
            <v-icon size="small" @click="deleteItem(item)">
                mdi-delete
            </v-icon>
        </template>
        <template v-slot:no-data>
            <v-btn color="primary" @click="initialize">
                Reset
            </v-btn>
        </template>
    </v-data-table>
</template>


<script>
import axios from 'axios';
import { useToast } from "vue-toastification";
export default {
    data: () => ({
        dialog: false,
        dialogDelete: false,
        headers: [
            { title: 'ID', key: 'id' },
            { title: 'Tên người đăng bài', key: 'postUserName' },
            { title: 'Số điện thoại người đăng bài', key: 'postUserPhoneNumber', sortable: false},
            { title: 'Nội dung bài viết', key: 'content' },
            { title: 'Chế độ hiển thị', key: 'audience' },
            { title: 'Ngày tạo', key: 'createdDate', sortable: false},
            { title: 'Lượt thích', key: 'likesCount', type: 'number', align: 'center' },
            { title: 'Lượt bình luận', key: 'commentsCount', type: 'number', align: 'center' },
            { title: 'Hành động', key: 'actions', sortable: false },
        ],
        posts: [],
        editedIndex: -1,
        editedItem: {
            id: null,
            postUserName: '',
            postUserPhoneNumber: '',
            content: '',
            audience: '',
            createdDate: '',
            likesCount: 0,
            commentsCount: 0,
        },
        defaultItem: {
            id: null,
            postUserName: '',
            postUserPhoneNumber: '',
            content: '',
            audience: '',
            createdDate: '',
            likesCount: 0,
            commentsCount: 0,
        },
    }),

    computed: {
        formTitle() {
            return this.editedIndex === -1 ? 'Thêm bài viết' : 'Chỉnh sửa bài viết'
        },
    },
    setup() {
		// Get toast interface
		const toast = useToast(

		);
		return { toast }
	},
    watch: {
        dialog(val) {
            val || this.close()
        },
        dialogDelete(val) {
            val || this.closeDelete()
        },
    },

    created() {
        this.initialize()
    },

    methods: {
        async initialize() {
            try {
                const result = await axios.get(`http://localhost:8181/v1/admin/getAllPosts`);
                if (result.status === 200) {
                    this.posts = result.data;
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

        editItem(item) {
            this.editedIndex = this.posts.indexOf(item)
            this.editedItem = Object.assign({}, item)
            this.dialog = true
        },

        deleteItem(item) {
            this.editedIndex = this.posts.indexOf(item)
            this.editedItem = Object.assign({}, item)
            this.dialogDelete = true
        },

        async deleteItemConfirm() {
            this.posts.splice(this.editedIndex, 1)
            await this.deletePost();
            this.closeDelete()
        },

        async deletePost() {
            try {
                const result = await axios.delete(`http://localhost:8181/v1/admin/deletePost/${this.editedItem.id}`);
                if (result.status === 200) {
                    console.log(result);
                    this.toast.success("Delete post successfully", { timeout: 1500 });
                    this.initialize();
                } else {
                    this.toast.error(result.data.message, { timeout: 1500 });
                }
            } catch (exception) {
                console.log("Loi roi");
                console.log(exception);
            }

        },

        close() {
            this.dialog = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },

        closeDelete() {
            this.dialogDelete = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },

        save() {
            if (this.editedIndex > -1) {
                Object.assign(this.posts[this.editedIndex], this.editedItem)
            } else {
                this.posts.push(this.editedItem)
            }
            this.close()
        },
    },
}
</script>

<style></style>