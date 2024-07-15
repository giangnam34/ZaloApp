<template>
    <div class=" flex flex-col justify-between space-y-[10px]">
        <div @click="onClick(1)" :class="{ isChoose: index === 1 }"
            class="cursor-pointer inline-flex relative items-center py-[10px] px-[10px] w-full text-sm font-medium rounded-md border-gray-200 hover:bg-gray-200 hover:text-gray-800  transition duration-400 ease-in-out">
            <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" class="mr-2 w-[25px] h-[25px] fill-current"
                fill="currentColor" viewBox="0 0 448 512">
                <path fill-rule="evenodd" clip-rule="evenodd"
                    d="M160 80c0-26.5 21.5-48 48-48h32c26.5 0 48 21.5 48 48V432c0 26.5-21.5 48-48 48H208c-26.5 0-48-21.5-48-48V80zM0 272c0-26.5 21.5-48 48-48H80c26.5 0 48 21.5 48 48V432c0 26.5-21.5 48-48 48H48c-26.5 0-48-21.5-48-48V272zM368 96h32c26.5 0 48 21.5 48 48V432c0 26.5-21.5 48-48 48H368c-26.5 0-48-21.5-48-48V144c0-26.5 21.5-48 48-48z" />
            </svg>
            Thống kê
        </div>
        <div @click="onClick(2)" :class="{ isChoose: index === 2 }"
            class="cursor-pointer inline-flex relative items-center py-[10px] px-[10px] w-full text-sm font-medium rounded-md border-gray-200 hover:bg-gray-200 hover:text-gray-800  transition duration-400 ease-in-out">
            <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" class="mr-2 w-[25px] h-[25px] fill-current"
                fill="currentColor" viewBox="0 0 640 512">
                <path fill-rule="evenodd" clip-rule="evenodd"
                    d="M96 128a128 128 0 1 1 256 0A128 128 0 1 1 96 128zM0 482.3C0 383.8 79.8 304 178.3 304h91.4C368.2 304 448 383.8 448 482.3c0 16.4-13.3 29.7-29.7 29.7H29.7C13.3 512 0 498.7 0 482.3zM609.3 512H471.4c5.4-9.4 8.6-20.3 8.6-32v-8c0-60.7-27.1-115.2-69.8-151.8c2.4-.1 4.7-.2 7.1-.2h61.4C567.8 320 640 392.2 640 481.3c0 17-13.8 30.7-30.7 30.7zM432 256c-31 0-59-12.6-79.3-32.9C372.4 196.5 384 163.6 384 128c0-26.8-6.6-52.1-18.3-74.3C384.3 40.1 407.2 32 432 32c61.9 0 112 50.1 112 112s-50.1 112-112 112z" />
            </svg>
            Quản lý người dùng
        </div>
        <div @click="onClick(3)" :class="{ isChoose: index === 3 }"
            class="cursor-pointer inline-flex relative items-center py-[10px] px-[10px] w-full text-sm font-medium rounded-md border-gray-200 hover:bg-gray-300  hover:text-gray-800 transition duration-400 ease-in-out">
            <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" class="mr-2 w-[25px] h-[25px] fill-current"
                fill="currentColor" viewBox="0 0 512 512">
                <path fill-rule="evenodd" clip-rule="evenodd"
                    d="M96 96c0-35.3 28.7-64 64-64H448c35.3 0 64 28.7 64 64V416c0 35.3-28.7 64-64 64H80c-44.2 0-80-35.8-80-80V128c0-17.7 14.3-32 32-32s32 14.3 32 32V400c0 8.8 7.2 16 16 16s16-7.2 16-16V96zm64 24v80c0 13.3 10.7 24 24 24H296c13.3 0 24-10.7 24-24V120c0-13.3-10.7-24-24-24H184c-13.3 0-24 10.7-24 24zm208-8c0 8.8 7.2 16 16 16h48c8.8 0 16-7.2 16-16s-7.2-16-16-16H384c-8.8 0-16 7.2-16 16zm0 96c0 8.8 7.2 16 16 16h48c8.8 0 16-7.2 16-16s-7.2-16-16-16H384c-8.8 0-16 7.2-16 16zM160 304c0 8.8 7.2 16 16 16H432c8.8 0 16-7.2 16-16s-7.2-16-16-16H176c-8.8 0-16 7.2-16 16zm0 96c0 8.8 7.2 16 16 16H432c8.8 0 16-7.2 16-16s-7.2-16-16-16H176c-8.8 0-16 7.2-16 16z" />
            </svg>
            Quản lý bài viết
        </div>
        <div @click="onClick(4)" :class="{ isChoose: index === 4 }"
            class="cursor-pointer inline-flex relative items-center py-[10px] px-[10px] w-full text-sm font-medium rounded-md border-gray-200 hover:bg-gray-300  hover:text-gray-800 transition duration-400 ease-in-out">
            <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" class="mr-2 w-[25px] h-[25px] fill-current"
                fill="currentColor" viewBox="0 0 640 512">
                <path fill-rule="evenodd" clip-rule="evenodd"
                    d="M211.2 96a64 64 0 1 0 -128 0 64 64 0 1 0 128 0zM32 256c0 17.7 14.3 32 32 32h85.6c10.1-39.4 38.6-71.5 75.8-86.6c-9.7-6-21.2-9.4-33.4-9.4H96c-35.3 0-64 28.7-64 64zm461.6 32H576c17.7 0 32-14.3 32-32c0-35.3-28.7-64-64-64H448c-11.7 0-22.7 3.1-32.1 8.6c38.1 14.8 67.4 47.3 77.7 87.4zM391.2 226.4c-6.9-1.6-14.2-2.4-21.6-2.4h-96c-8.5 0-16.7 1.1-24.5 3.1c-30.8 8.1-55.6 31.1-66.1 60.9c-3.5 10-5.5 20.8-5.5 32c0 17.7 14.3 32 32 32h224c17.7 0 32-14.3 32-32c0-11.2-1.9-22-5.5-32c-10.8-30.7-36.8-54.2-68.9-61.6zM563.2 96a64 64 0 1 0 -128 0 64 64 0 1 0 128 0zM321.6 192a80 80 0 1 0 0-160 80 80 0 1 0 0 160zM32 416c-17.7 0-32 14.3-32 32s14.3 32 32 32H608c17.7 0 32-14.3 32-32s-14.3-32-32-32H32z" />
            </svg>
            Quản lý nhóm
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            index: 1,
        }
    },
    methods: {
        // hide show side bar
        toggleSideBar() {
            try {
                this.showSide = !this.showSide;
            } catch (exception) {
                console.log("Error in toggleSideBar", exception);
            }
        },
        // toggle user 
        toggleDrop() {
            try {
                this.showDropDown = !this.showDropDown;
            } catch (exception) {
                console.log("Error in toggleDrop", exception);
            }
        },
        logout() {
            try {
                localStorage.removeItem("token");
                localStorage.setItem("isValid", false);
                localStorage.removeItem("user");
                this.$emit("userLoggedIn", '');
                event.preventDefault();
                event.stopPropagation();
            } catch (exception) {
                console.log("Error in logout", exception);
            }
        },
        onClick(index) {
            try {
                this.index = index;
                this.$emit('pageSelected', index);
            } catch (exception) {
                console.log("Error in onClick", exception);
            }
        },
    }

}
</script>

<style>
.isChoose {
    background-color: #e5e7eb;
    color: #1f2937;
}
</style>