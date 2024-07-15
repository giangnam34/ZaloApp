<template>
    <div class="limiter">
        <div class="container">
            <div class="wrap-signup" v-if="!showOTP">
                <div class="validate-form">
                    <span class="title">Đăng ký</span>
                    <div class="name">
                        <span class="label-input">Tên</span>
                        <div class="name-input">
                            <div class="icon-user">
                                <font-awesome-icon icon="fa-solid fa-user" />
                            </div>
                            <input class="input" type="text" v-model="username" name="name"
                                placeholder="Nhập tên người dùng" @blur="validateName" ref="nameInput" required
                                tabindex="1" @keyup.enter="signUp">
                        </div>
                        <em class="error" v-if="isError === true">{{ validationErrorName }}</em>
                        <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                    </div>
                    <div class="phone">
                        <span class="label-input">Số điện thoại</span>
                        <div class="phone-input">
                            <div class="icon-phone">
                                <font-awesome-icon icon="fa-solid fa-phone" />
                            </div>
                            <input class="input" type="number" v-model="phoneNumber" name="phone"
                                placeholder="Nhập số điện thoại" @blur="validatePhoneNumber" ref="phoneInput" required
                                tabindex="2" @keyup.enter="signUp">
                        </div>
                        <em class="error" v-if="isError === true">{{ validationErrorPhoneNumber }}</em>
                        <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                    </div>
                    <div class="phone">
                        <span class="label-input">Ngày sinh</span>
                        <div class="phone-input">
                            <div class="icon-phone">
                                <font-awesome-icon icon="fa-solid fa-calendar-days" />
                            </div>
                            <flat-pickr class="input" v-model="birthDay" placeholder="Chọn ngày sinh"
                                :config="flatpickrConfig" @change="validateBirthday" @keyup.enter="signUp"></flat-pickr>
                        </div>
                        <em class="error" v-if="isError === true">{{ validationErrorBirthday }}</em>
                        <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                    </div>
                    <div class="password">
                        <span class="label-input">Mật khẩu</span>
                        <div class="password-input">
                            <div class="icon-password">
                                <font-awesome-icon icon="fa-solid fa-key" />
                            </div>
                            <input class="input" type="password" v-model="password" name="password"
                                placeholder="Nhập mật khẩu" @blur="validatePassword" ref="passwordInput" required
                                tabindex="3" @keyup.enter="signUp">
                            <button class="toggle-password" @click="togglePasswordVisibility">
                                <font-awesome-icon :icon="showPassword ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'" />
                            </button>
                        </div>
                        <em class="error" v-if="isError === true">{{ validationErrorPassword }}</em>
                        <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                    </div>
                    <div class="password">
                        <span class="label-input">Xác nhận mật khẩu</span>
                        <div class="password-input">
                            <div class="icon-password">
                                <font-awesome-icon icon="fa-solid fa-key" />
                            </div>
                            <input class="input" type="password" v-model="confirmPassword" name="confirmPassword"
                                placeholder="Nhập lại mật khẩu" @blur="validateConfirmPassword"
                                ref="confirmPasswordInput" required tabindex="5" @keyup.enter="signUp">
                            <button class="toggle-password" @click="togglePasswordVisibility">
                                <font-awesome-icon :icon="showPassword ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'" />
                            </button>
                        </div>
                        <em class="error" v-if="isError === true">{{ validationErrorConfirmPassword }}</em>
                        <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                    </div>
                    <em class="error" v-if="isError === true">{{ validationError }}</em>
                    <div class="container-signup-button">
                        <div class="wrap-signup-button">
                            <div class="signup-bgbutton"></div>
                            <button class="signup-button" @click="signUp">Đăng ký</button>
                        </div>
                    </div>
                    <div class="signin">
                        <span class="signin-title">Đã có tài khoản?</span>
                        <span class="signin-link" :class="{ 'hovered': hoveredItem === 'signin' }"
                            @mouseenter="(hoveredItem = 'signin')" @mouseleave="hoveredItem = ''"
                            @click="showSignIn">Đăng
                            nhập</span>
                    </div>
                </div>
            </div>
            <div class="wrap-otp" v-if="showOTP">
                <OTP style="width: 100%;" @update:showOTP="updateShowOTP" :phoneNumber="phoneNumber"
                    @update:showingPage="showSignIn"></OTP>
            </div>
        </div>
    </div>
</template>

<script>

import axios from 'axios';
import OTP from './OTP.vue';
import { useToast } from "vue-toastification";
import FlatPickr from 'vue-flatpickr-component';
import 'flatpickr/dist/flatpickr.css';

export default {
    data() {
        return {
            showPassword: false,
            isError: true,
            hoveredItem: '',
            username: '',
            phoneNumber: '',
            birthDay: null,
            password: '',
            confirmPassword: '',
            validationErrorName: '',
            validationErrorPhoneNumber: '',
            validationErrorPassword: '',
            validationErrorConfirmPassword: '',
            validationErrorBirthday: '',
            validationError: '',
            registerUser: null,
            showOTP: false,
            flag1: true,
            flag2: true,
            flag3: true,
            flag4: true,
            flatpickrConfig: {
                dateFormat: 'Y-m-d',
            }
        };
    },
    components: {
        OTP,
        FlatPickr
    }, setup() {
        // Get toast interface
        const toast = useToast();
        return { toast }
    },
    methods: {
        validateBirthday() {
            try {
                if (!this.birthDay) {
                    this.isError = true;
                    this.validationErrorBirthday = 'Vui lòng chọn ngày sinh.';
                    return;
                }

                let regexDate = /^\d{4}-\d{2}-\d{2}$/;
                if (!regexDate.test(this.birthDay)) {
                    this.isError = true;
                    this.validationErrorBirthday = 'Định dạng ngày sinh không hợp lệ (VD: YYYY-MM-DD).';
                    return;
                }

                let selectedDate = new Date(this.birthDay);
                let currentDate = new Date();

                if (selectedDate > currentDate) {
                    this.isError = true;
                    this.validationErrorBirthday = 'Ngày sinh không thể lớn hơn ngày hiện tại.';
                    return;
                }

                if (isNaN(selectedDate.getTime())) {
                    this.isError = true;
                    this.validationErrorBirthday = 'Ngày sinh không hợp lệ.';
                    return;
                }

                this.isError = false;
                this.validationErrorBirthday = '';
            } catch (exception) {
                console.log("Error in validateBirthday ", exception);
            }
        },
        togglePasswordVisibility() {
            try {
                this.showPassword = !this.showPassword;
                const passwordInput = document.querySelector('input[name="password"]');
                const confirmPasswordInput = document.querySelector('input[name="confirmPassword"]');
                if (this.showPassword) {
                    passwordInput.setAttribute('type', 'text');
                    confirmPasswordInput.setAttribute('type', 'text');
                } else {
                    passwordInput.setAttribute('type', 'password');
                    confirmPasswordInput.setAttribute('type', 'password');
                }
            } catch (exception) {
                console.log("Error in togglePasswordVisibility ", exception);
            }
        },
        validatePhoneNumber() {
            try {
                this.phoneNumber = this.phoneNumber + '';
                if (this.phoneNumber.length == 9 && this.phoneNumber.at(0) != '0') {
                    this.phoneNumber = '0' + this.phoneNumber;
                }
                const phoneNumber = this.phoneNumber;
                const isValidPhoneNumber = /^0\d{9}$/.test(phoneNumber);

                if (!isValidPhoneNumber) {
                    this.isError = true;
                    this.validationErrorPhoneNumber = 'Số điện thoại không hợp lệ!';
                    this.flag2 = true;
                } else {
                    this.validationErrorPhoneNumber = '';
                    this.flag2 = false;
                }
                if (!(this.flag1 || this.flag2 || this.flag3 || this.flag4)) this.isError = false;
            } catch (exception) {
                console.log("Error in validatePhoneNumber ", exception);
            }
        },
        validatePassword() {
            try {
                const password = this.password;
                const confirmPassword = this.confirmPassword;
                const regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,20}$/;
                if (password.length == 0) {
                    this.isError = true;
                    this.validationErrorPassword = 'Vui lòng nhập mật khẩu!';
                    this.flag3 = true;
                } else if (!password.match(regex)) {
                    this.isError = true;
                    this.validationErrorPassword = "Vui lòng nhập mật khẩu ít nhất 8 ký tự gồm chữ hoa, chữ thường, số và 1 ký tự đặc biệt!";
                    this.flag3 = true;
                } else {
                    this.validationErrorPassword = '';
                }
                if ((password == confirmPassword) && confirmPassword.length != 0) {
                    this.validationErrorPassword = '';
                    this.flag3 = false;
                }
                if (!(this.flag1 || this.flag2 || this.flag3 || this.flag4)) this.isError = false;
            } catch (exception) {
                console.log("Error in validatePassword ", exception);
            }
        },
        validateConfirmPassword() {
            try {
                const password = this.password;
                const confirmPassword = this.confirmPassword;
                if (confirmPassword.length == 0) {
                    this.isError = true;
                    this.validationErrorConfirmPassword = 'Vui lòng nhập xác nhận mật khẩu!';
                    this.flag4 = true;
                }
                if (!(password == confirmPassword)) {
                    this.isError = true;
                    this.validationErrorConfirmPassword = 'Mật khẩu và xác nhận mật khẩu không khớp!';
                    this.flag3 = true;
                    this.flag4 = true;
                } else {
                    this.validationErrorConfirmPassword = '';
                    this.flag3 = false;
                    this.flag4 = false;
                }
                if (!(this.flag1 || this.flag2 || this.flag3 || this.flag4)) this.isError = false;
            } catch (exception) {
                console.log("Error in validateConfirmPassword ", exception);
            }
        },
        validateName() {
            try {
                const username = this.username;
                if (username.length == 0) {
                    this.isError = true;
                    this.validationErrorName = 'Vui lòng nhập tên người dùng!';
                    this.flag1 = true;
                }
                else {
                    this.validationErrorName = '';
                    this.flag1 = false;
                }
                if (!(this.flag1 || this.flag2 || this.flag3 || this.flag4)) this.isError = false;
            } catch (exception) {
                console.log("Error in validateName ", exception);
            }
        },
        showSignIn() {
            try {
                this.$emit('update:showingPage', 'signIn');
            } catch (exception) {
                console.log("Error in showSignIn ", exception);
            }
        },
        updateShowOTP(value) {
            try {
                this.showOTP = value;
            } catch (exception) {
                console.log("Error in updateShowOTP ", exception);
            }
        },
        async signUp() {
            try {
                if (this.phoneNumber == '' && this.password == '' && this.username == '' && this.confirmPassword == '') {
                    this.isError = true;
                    this.validationError = "Vui lòng nhập đầy đủ thông tin!"
                } else if (this.phoneNumber == '') {
                    this.isError = true;
                    this.validationError = "Vui lòng nhập số điện thoại!"
                } else if (this.password == '') {
                    this.isError = true;
                    this.validationError = "Vui lòng nhập mật khẩu!"
                } else if (this.username == '') {
                    this.isError = true;
                    this.validationError = "Vui lòng nhập tên người dùng!"
                } else if (this.confirmPassword == '') {
                    this.isError = true;
                    this.validationError = "Vui lòng nhập xác nhận mật khẩu!"
                } else {

                    this.validateName();
                    this.validatePhoneNumber();
                    this.validatePassword();
                    this.validateConfirmPassword();

                    if (!this.isError) {
                        try {
                            let selectedDate = new Date(this.birthDay);
                            const registerUser = {
                                fullName: this.username,
                                phoneNumber: this.phoneNumber,
                                password: this.password,
                                reEnterPassword: this.confirmPassword,
                                birthDay: selectedDate
                            }

                            console.log(registerUser);

                            const response = await axios.post('auth/sign-up', registerUser);

                            console.log(response);

                            // Kiểm tra trạng thái phản hồi
                            if (response.status === 200) {

                                this.toast.success(response.data.msg, { timeout: 3000 });

                                this.showOTP = true;

                            } else {
                                console.log(response);
                                console.error('Đăng ký không thành công:', response.statusText);
                                this.isError = true;
                                this.validationError = 'Đã xảy ra lỗi khi đăng ký. Vui lòng thử lại!';
                            }
                        } catch (error) {
                            console.error('Đăng ký không thành công:');
                            console.log(error.response);
                            if (error.response) {
                                console.error('Server responded with an error status:', error.response.status);

                                if (error.response.status === 400) {
                                    this.isError = true;
                                    this.validationError = error.response.data.msg;
                                } else {
                                    this.isError = true;
                                    this.validationError = error.response.data.msg;
                                }
                            } else if (error.request) {
                                console.error('No response received from the server:', error.request);
                                this.isError = true;
                                this.validationError = 'Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!';
                            } else {
                                console.error('Error setting up the request:', error.message);
                                this.isError = true;
                                this.validationError = 'Đã xảy ra lỗi khi đăng ký. Vui lòng thử lại!';
                            }
                        }

                    }
                }
            } catch (exception) {
                console.log("Error in signUp ", exception);
            }
        },
        handleEnterKey(event) {
            try {
                if (event.key === 'Enter') {
                    this.signUp();
                }
            } catch (exception) {
                console.log("Error in handleEnterKey ", exception);
            }
        },
    }
};
</script>

<style scoped lang="scss">
.limiter {
    width: 100%;
    margin: 0 auto;

    .hovered {
        cursor: pointer;
        color: #235dc7;
    }

    .container {
        display: flex;
        width: 100%;
        max-width: 100%;
        min-height: 100vh;
        flex-wrap: wrap;
        justify-content: center;
        align-items: center;
        padding: 15px;
        background-image: url('../assets/img/bg-01.jpg');
        background-size: cover;

        .wrap-signup {
            width: 500px;
            background: #fff;
            border-radius: 10px;
            overflow: hidden;
            padding: 65px 55px 54px 55px;

            .validate-form {

                .forgot-password {
                    text-align: right;
                    padding-bottom: 31px;
                    padding-top: 8px;
                }

                .error {
                    display: block;
                    padding: 0;
                    color: #DD4B39;
                    font-size: 0.875rem;
                }

                .title {
                    display: block;
                    font-family: Poppins-Bold;
                    color: #333333;
                    font-size: 39px;
                    line-height: 1.2;
                    text-align: center;
                    padding-bottom: 49px;
                }

                .name,
                .phone,
                .password {

                    position: relative;
                    margin-bottom: 23px;

                    .label-input {
                        font-family: Poppins-Regular;
                        font-size: 20px;
                        color: #333;
                        line-height: 1.5;
                        padding-left: 7px;
                    }

                    .phone-input input[type="number"] {
                        /* Firefox */
                        -moz-appearance: textfield;

                        /* Chrome, Safari, Edge */
                        appearance: textfield;
                    }

                    /* Hide spinners in Firefox */
                    .phone-input input[type="number"]::-webkit-inner-spin-button,
                    .phone-input input[type="number"]::-webkit-outer-spin-button {
                        -webkit-appearance: none;
                        margin: 0;
                    }

                    /* Hide spinners in Chrome, Safari, Edge */
                    .phone-input input[type="number"] {
                        -moz-appearance: textfield;
                    }

                    .phone-input,
                    .password-input,
                    .name-input {
                        display: flex;


                        .input {
                            outline: none;
                            border: none;
                            font-family: Poppins-Medium;
                            font-size: 18px;
                            color: #333;
                            line-height: 1.2;
                            display: block;
                            width: 100%;
                            height: 55px;
                            background: 0 0;
                            padding: 0 7px 0 43px;
                        }

                        .icon-phone,
                        .icon-password,
                        .icon-user {
                            display: flex;
                            align-items: center;
                            margin-right: 4px;
                            margin-left: 14px;
                        }
                    }
                }

                .container-signup-button {
                    display: flex;
                    flex-wrap: wrap;
                    justify-content: center;
                    margin-bottom: 23px;

                    .wrap-signup-button {
                        width: 100%;
                        display: block;
                        position: relative;
                        z-index: 1;
                        border-radius: 25px;
                        overflow: hidden;
                        margin: 0 auto;
                        box-shadow: 0 5px 30px 0 rgba(3, 216, 222, .2);
                        -moz-box-shadow: 0 5px 30px 0 rgba(3, 216, 222, .2);
                        -webkit-box-shadow: 0 5px 30px 0 rgba(3, 216, 222, .2);
                        -o-box-shadow: 0 5px 30px 0 rgba(3, 216, 222, .2);
                        -ms-box-shadow: 0 5px 30px 0 rgba(3, 216, 222, .2);

                        .signup-bgbutton {
                            position: absolute;
                            z-index: -1;
                            width: 300%;
                            height: 100%;
                            background: #a64bf4;
                            background: linear-gradient(to right, #00dbde, #fc00ff, #00dbde, #fc00ff);
                            left: -100%;
                            transition: all .4s;
                        }

                        .signup-button {
                            touch-action: manipulation;
                            font-family: Poppins-Medium;
                            font-size: 16px;
                            color: #fff;
                            line-height: 1.2;
                            text-transform: uppercase;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            padding: 0 20px;
                            width: 100%;
                            height: 50px;
                            outline: none !important;
                            border: none;
                            background-color: #a64bf4;
                            background-image: linear-gradient(to right, #00dbde, #fc00ff);
                        }
                    }
                }

                .signin {
                    display: flex;
                    flex-direction: column;
                    align-items: center;

                    .signin-title {
                        font-family: Poppins-Regular;
                        font-size: 18px;
                        line-height: 1.5;
                        color: #666;
                        margin-bottom: 2px;
                    }

                    .signin-link {
                        font-family: Poppins-Regular;
                        font-size: 20px;
                        line-height: 1.5;
                        text-transform: uppercase;
                    }
                }
            }

        }

        .wrap-otp {
            width: 500px;
            background: #fff;
            border-radius: 10px;
            overflow: hidden;
            padding: 65px 55px 54px 55px;

            .validate-form {
                .title {
                    display: block;
                    font-family: Poppins-Bold;
                    color: #333333;
                    font-size: 39px;
                    line-height: 1.2;
                    text-align: center;
                    padding-bottom: 49px;
                }

                .error {
                    display: block;
                    padding: 0;
                    color: #DD4B39;
                    font-size: 0.875rem;
                }

                .otp {

                    position: relative;
                    margin-bottom: 23px;

                    .otp-input {
                        display: flex;

                        .input {
                            outline: none;
                            border: none;
                            font-family: Poppins-Medium;
                            font-size: 18px;
                            color: #333;
                            line-height: 1.2;
                            display: block;
                            width: 100%;
                            height: 55px;
                            background: 0 0;
                            padding: 0 7px 0 43px;
                        }

                        .icon-otp {
                            display: flex;
                            align-items: center;
                            margin-right: 4px;
                            margin-left: 14px;
                        }
                    }

                    .label-input {
                        font-family: Poppins-Regular;
                        font-size: 20px;
                        color: #333;
                        line-height: 1.5;
                        padding-left: 7px;
                    }
                }

                .container-otp-button {
                    display: flex;
                    flex-wrap: wrap;
                    justify-content: center;
                    margin-bottom: 23px;

                    .wrap-otp-button {
                        width: 100%;
                        display: block;
                        position: relative;
                        z-index: 1;
                        border-radius: 25px;
                        overflow: hidden;
                        margin: 0 auto;
                        box-shadow: 0 5px 30px 0 rgba(3, 216, 222, .2);
                        -moz-box-shadow: 0 5px 30px 0 rgba(3, 216, 222, .2);
                        -webkit-box-shadow: 0 5px 30px 0 rgba(3, 216, 222, .2);
                        -o-box-shadow: 0 5px 30px 0 rgba(3, 216, 222, .2);
                        -ms-box-shadow: 0 5px 30px 0 rgba(3, 216, 222, .2);

                        .otp-bgbutton {
                            position: absolute;
                            z-index: -1;
                            width: 300%;
                            height: 100%;
                            background: #a64bf4;
                            background: linear-gradient(to right, #00dbde, #fc00ff, #00dbde, #fc00ff);
                            left: -100%;
                            transition: all .4s;
                        }

                        .otp-button {
                            touch-action: manipulation;
                            font-family: Poppins-Medium;
                            font-size: 16px;
                            color: #fff;
                            line-height: 1.2;
                            text-transform: uppercase;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            padding: 0 20px;
                            width: 100%;
                            height: 50px;
                            outline: none !important;
                            border: none;
                            background-color: #a64bf4;
                            background-image: linear-gradient(to right, #00dbde, #fc00ff);
                        }
                    }
                }

                .back-to-signup {
                    display: flex;
                    flex-direction: column;
                    align-items: center;

                    .signup-link {
                        font-family: Poppins-Regular;
                        font-size: 20px;
                        line-height: 1.5;
                        text-transform: uppercase;
                    }
                }

            }
        }
    }
}
</style>