<template>
    <h3 class="text-2xl font-bold text-center pb-[10px]">Thống kê</h3>
    <v-container class="bg-surface-variant">
        <v-row no-gutters justify="center">
            <v-col cols="12" md="6">
                <v-sheet class="pa-2 ma-2">
                    Tổng số người dùng: {{ generalInfo ? generalInfo.membersCount : 'Đang tải...' }}
                </v-sheet>
            </v-col>
            <v-col cols="12" md="6">
                <v-sheet class="pa-2 ma-2">
                    Số lượng người dùng trực tuyến: {{ generalInfo ? generalInfo.onlineMembersCount : 'Đang tải...' }}
                </v-sheet>
            </v-col>
            <v-col cols="12" md="6">
                <v-sheet class="pa-2 ma-2">
                    Tổng số lượng nhóm: {{ generalInfo ? generalInfo.groupsCount : 'Đang tải...' }}
                </v-sheet>
            </v-col>
            <v-col cols="12" md="6">
                <v-sheet class="pa-2 ma-2">
                    Tổng số lượng bài viết: {{ generalInfo ? generalInfo.postsCount : 'Đang tải...' }}
                </v-sheet>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';
export default {
    data() {
        return {
            generalInfo: null,
        }
    },
    async created() {
        await this.initialize();
    },
    methods: {
        async initialize() {
            try {
                const result = await axios.get(`http://localhost:8181/v1/admin/getGeneralInfo`);
                if (result.status === 200) {
                    this.generalInfo = result.data;
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
    }
}
</script>

<style>

</style>