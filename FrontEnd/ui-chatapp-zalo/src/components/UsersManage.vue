<template>
    <h3 class="text-2xl font-bold text-left pb-[10px]">Quản lý người dùng</h3>
    <v-data-table :headers="headers" :items="users" :sort-by="[{ key: 'id', order: 'asc' }]">
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
                                    <v-text-field v-model="editedItem.fullName" label="Họ và tên"
                                        readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.birthDay" label="Ngày sinh"
                                        readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.postsCount" label="Tổng bài viết" type="number"
                                        readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.friendsCount" label="Tổng bạn bè" type="number"
                                        readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.groupsCount" label="Tổng nhóm tham gia"
                                        type="number" readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-switch v-model="editedItem.isLock" label="Đã bị khóa" readonly></v-switch>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-switch v-model="editedItem.isConfirm" label="Đã xác nhận"></v-switch>
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
                    <v-card-title class="text-h5 item-center">Bạn có chắc muốn khóa tài khoản này?</v-card-title>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="blue-darken-1" variant="text" @click="closeDelete">Hủy</v-btn>
                        <v-btn color="blue-darken-1" variant="text" @click="deleteItemConfirm">Khóa</v-btn>
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
            { title: 'Họ và tên', key: 'fullName' },
            { title: 'Ngày sinh', key: 'birthDay', sortable: false },
            { title: 'Tổng bài viết', key: 'postsCount', align: 'center' },
            { title: 'Tổng bạn bè', key: 'friendsCount', align: 'center' },
            { title: 'Tổng nhóm tham gia', key: 'groupsCount', align: 'center' },
            { title: 'Đã bị khóa', key: 'isLock', sortable: false, align: 'center' },
            { title: 'Đã xác nhận', key: 'isConfirm', sortable: false, align: 'center' },
            { title: 'Hành động', key: 'actions', sortable: false, align: 'center' },
        ],
        users: [],
        editedIndex: -1,
        editedItem: {
            id: null,
            fullName: '',
            birthDay: '',
            postsCount: 0,
            friendsCount: 0,
            groupsCount: 0,
            isLock: false,
            isConfirm: true,
        },
        defaultItem: {
            id: null,
            fullName: '',
            birthDay: '',
            postsCount: 0,
            friendsCount: 0,
            groupsCount: 0,
            isLock: false,
            isConfirm: true,
        },
    }),

    computed: {
        formTitle() {
            return this.editedIndex === -1 ? 'Thêm người dùng' : 'Chỉnh sửa người dùng'
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
                try {
                    const result = await axios.get(`http://localhost:8181/v1/admin/getAllUsers`);
                    if (result.status === 200) {
                        this.users = result.data;
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
            } catch (error) {
                console.error('Failed to fetch users:', error);
            }
        },

        editItem(item) {
            try {
                this.editedIndex = this.users.indexOf(item);
                this.editedItem = Object.assign({}, item);
                this.dialog = true;
            } catch (error) {
                console.error("Error in editItem:", error);
                // Optionally handle or log the error further if needed
            }
        },


        deleteItem(item) {
            try {
                this.editedIndex = this.users.indexOf(item);
                this.editedItem = Object.assign({}, item);
                this.dialogDelete = true;
            } catch (error) {
                console.error("Error in deleteItem:", error);
                // Optionally handle or log the error further if needed
            }
        },

        async deleteItemConfirm() {
            try {
                this.users.splice(this.editedIndex, 1);
                await this.lockUser();
                this.closeDelete();
            } catch (error) {
                console.error("Error in deleteItemConfirm:", error);
                // Optionally handle or log the error further if needed
            }
        },

        async lockUser() {
            try {
                const result = await axios.delete(`http://localhost:8181/v1/admin/lockUser/${this.editedItem.id}`);
                if (result.status === 200) {
                    console.log(result);
                    this.toast.success("Khóa người dùng thành công!", { timeout: 1500 });
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
            try {
                this.dialog = false;
                this.$nextTick(() => {
                    this.editedItem = Object.assign({}, this.defaultItem);
                    this.editedIndex = -1;
                });
            } catch (error) {
                console.error("Error in close:", error);
                // Optionally handle or log the error further if needed
            }
        },

        closeDelete() {
            try {
                this.dialogDelete = false;
                this.$nextTick(() => {
                    this.editedItem = Object.assign({}, this.defaultItem);
                    this.editedIndex = -1;
                });
            } catch (error) {
                console.error("Error in closeDelete:", error);
                // Optionally handle or log the error further if needed
            }
        },

        save() {
            try {
                if (this.editedIndex > -1) {
                    Object.assign(this.users[this.editedIndex], this.editedItem);
                } else {
                    this.users.push(this.editedItem);
                }
                this.close();
            } catch (error) {
                console.error("Error in save:", error);
                // Optionally handle or log the error further if needed
            }
        },
    },
}
</script>

<style></style>