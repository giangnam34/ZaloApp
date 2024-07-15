<template>
    <h3 class="text-2xl font-bold text-left pb-[10px]">Quản lý nhóm</h3>
    <v-data-table :headers="headers" :items="groups" :sort-by="[{ key: 'id', order: 'asc' }]">
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
                                    <v-text-field v-model="editedItem.groupName" label="Tên nhóm"
                                        readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.membersCount" label="Tổng số thành viên"
                                        type="number" readonly></v-text-field>
                                </v-col>
                                <v-col cols="12" md="6">
                                    <v-text-field v-model="editedItem.messagesCount" label="Tổng số tin nhắn"
                                        type="number" readonly></v-text-field>
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
                    <v-card-title class="text-h5 item-center">Bạn có chắc muốn xóa nhóm này?</v-card-title>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="blue-darken-1" variant="text" @click="closeDelete">Hủy</v-btn>
                        <v-btn color="blue-darken-1" variant="text" @click="deleteItemConfirm">Xóa</v-btn>
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
            { title: 'Tên nhóm', key: 'groupName' },
            { title: 'Tổng số thành viên', key: 'membersCount', align: 'center' },
            { title: 'Tổng số tin nhắn', key: 'messagesCount', align: 'center' },
            { title: 'Hành động', key: 'actions', sortable: false, align: 'center' },
        ],
        groups: [],
        editedIndex: -1,
        editedItem: {
            id: null,
            groupName: '',
            membersCount: 0,
            messagesCount: 0,
        },
        defaultItem: {
            id: null,
            groupName: '',
            membersCount: 0,
            messagesCount: 0,
        },
    }),
    setup() {
        // Get toast interface
        const toast = useToast(

        );
        return { toast }
    },
    computed: {
        formTitle() {
            return this.editedIndex === -1 ? 'Thêm nhóm' : 'Chỉnh sửa nhóm'
        },
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
                const result = await axios.get(`http://localhost:8181/v1/admin/getAllGroups`);
                if (result.status === 200) {
                    this.groups = result.data;
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
            try {
                this.editedIndex = this.groups.indexOf(item);
                this.editedItem = Object.assign({}, item);
                this.dialog = true;
            } catch (exception) {
                console.log("Error in editItem", exception);
            }
        },

        deleteItem(item) {
            try {
                this.editedIndex = this.groups.indexOf(item);
                this.editedItem = Object.assign({}, item);
                this.dialogDelete = true;
            } catch (exception) {
                console.log("Error in deleteItem", exception);
            }
        },

        async deleteItemConfirm() {
            try {
                this.groups.splice(this.editedIndex, 1);
                await this.deleteRoom();
                this.closeDelete();
            } catch (exception) {
                console.log("Error in deleteItemConfirm", exception);
            }
        },
        async deleteRoom() {
            try {
                const result = await axios.delete(`http://localhost:8181/v1/admin/deleteGroupChat/${this.editedItem.id}`);
                if (result.status === 200) {
                    console.log(result);
                    this.toast.success("Xóa nhóm thành công!", { timeout: 1500 });
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
            } catch (exception) {
                console.log("Error in close", exception);
            }
        },

        closeDelete() {
            try {
                this.dialogDelete = false;
                this.$nextTick(() => {
                    this.editedItem = Object.assign({}, this.defaultItem);
                    this.editedIndex = -1;
                });
            } catch (exception) {
                console.log("Error in closeDelete", exception);
            }
        },

        save() {
            try {
                if (this.editedIndex > -1) {
                    Object.assign(this.groups[this.editedIndex], this.editedItem);
                } else {
                    this.groups.push(this.editedItem);
                }
                this.close();
            } catch (exception) {
                console.log("Error in save", exception);
            }
        },

    },
}
</script>

<style></style>
