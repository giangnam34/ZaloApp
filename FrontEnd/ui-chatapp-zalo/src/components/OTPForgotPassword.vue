<template>
    <div class="validate-form">
        <span class="title">Đổi mật khẩu</span>
        <div class="password">
            <span class="label-input">Mật khẩu</span>
            <div class="password-input">
                <div class="icon-password">
                    <font-awesome-icon icon="fa-solid fa-key" />
                </div>
                <input class="input" type="password" v-model="password" name="password" placeholder="Nhập mật khẩu"
                    @blur="validatePassword" ref="passwordInput" required tabindex="1" @keyup.enter="confirm">
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
                    placeholder="Nhập lại mật khẩu" @blur="validateConfirmPassword" ref="confirmPasswordInput" required
                    tabindex="2" @keyup.enter="confirm">
                <button class="toggle-password" @click="togglePasswordVisibility">
                    <font-awesome-icon :icon="showPassword ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'" />
                </button>
            </div>
            <em class="error" v-if="isError === true">{{ validationErrorConfirmPassword }}</em>
            <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
        </div>
        <div class="otp">
            <span class="label-input">OTP</span>
            <div class="otp-input">
                <div class="icon-otp">
                    <font-awesome-icon icon="fa-solid fa-unlock-keyhole" />
                </div>
                <input class="input" type="text" v-model="otp" name="otp" placeholder="Nhập OTP" required
                    @keyup.enter="confirm" @blur="validateOTP" tabindex="5">
                <button class="resend-otp" @click="sendOtp">Gửi lại OTP</button>
            </div>
            <em class="error" v-if="isError === true">{{ validationError }}</em>
            <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
        </div>
        <div class="container-otp-button">
            <div class="wrap-otp-button">
                <div class="otp-bgbutton"></div>
                <button class="otp-button" @click="confirm">Xác nhận</button>
            </div>
        </div>
        <div class="back-to-signup">
            <span class="signup-link" :class="{ 'hovered': hoveredItem === 'signup' }"
                @mouseenter="(hoveredItem = 'signup')" @mouseleave="hoveredItem = ''" @click="showSignUp">Quay
                lại</span>
        </div>
    </div>
</template>


<script>

import axios from 'axios';
import { useToast } from "vue-toastification";

export default {
    data() {
        return {
            isError: true,
            hoveredItem: '',
            validationErrorPassword: '',
            validationErrorConfirmPassword: '',
            validationErrorOTP: '',
            validationError: '',
            otp: '',
            showPassword: false,
            password: '',
            confirmPassword: '',
            flag1: true,
            flag3: true,
            flag4: true
        };
    },
    setup() {
        // Get toast interface
        const toast = useToast();
        return { toast }
    },
    props: ['phoneNumber'],
    methods: {
        validatePassword() {
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
                if ((password == confirmPassword) && confirmPassword.length != 0) {
                    this.validationErrorPassword = '';
                    this.flag3 = false;
                } else {
                    this.isError = true;
                    this.flag3 = true;
                }
            }

            if (!(this.flag1 || this.flag3 || this.flag4)) this.isError = false;
        },
        validateConfirmPassword() {
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
            this.validatePassword();
            if (!(this.flag1 || this.flag3 || this.flag4)) this.isError = false;
        },
        validateOTP() {
            const otpRegex = /^\d{6}$/;

            if (!this.otp.match(otpRegex)) {
                this.isError = true;
                this.validationError = "Mã OTP không hợp lệ!";
                this.flag1 = true;
            } else {
                this.validationError = "";
                this.flag1 = false;
            }
            if (!(this.flag1 || this.flag3 || this.flag4)) this.isError = false;
        },
        togglePasswordVisibility() {
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
        },
        showSignIn() {
            this.$emit('update:showingPage', 'signIn');
        },
        showSignUp() {
            this.$emit('update:showOTP', false);
        },
        handleEnterKey(event) {
            if (event.key === 'Enter') {
                this.confirm();
            }
        },
        async confirm() {
            if (this.password == '' && this.otp == '' && this.confirmPassword == '') {
                this.isError = true;
                this.validationError = "Vui lòng nhập đầy đủ thông tin!"
            } else if (this.otp == '') {
                this.isError = true;
                this.validationError = "Vui lòng nhập mã OTP!"
            } else if (this.password == '') {
                this.isError = true;
                this.validationError = "Vui lòng nhập mật khẩu!"
            } else if (this.confirmPassword == '') {
                this.isError = true;
                this.validationError = "Vui lòng nhập xác nhận mật khẩu!"
            } else {

                this.validateOTP();
                this.validatePassword();
                this.validateConfirmPassword();
                if (!this.isError) {
                    try {

                        const OTPResponse = {
                            phoneNumber: this.phoneNumber,
                            otpCode: this.otp,
                            newPassword: this.password,
                            reEnterPassword: this.confirmPassword,
                        }

                        console.log(OTPResponse);

                        const response = await axios.put('auth/forgetPassword', OTPResponse);

                        console.log(response);

                        // Kiểm tra trạng thái phản hồi
                        if (response.status === 200) {

                            this.toast.success(response.data, { timeout: 3000 });

                            this.showSignIn();

                        } else {
                            console.error('Xác thực không thành công:', response.statusText);
                            this.isError = true;
                            this.validationError = response.data;
                        }
                    } catch (error) {
                        if (error.response) {
                            console.error('Server responded with an error status:', error.response.status);

                            if (error.response.status === 400) {
                                this.isError = true;
                                this.validationError = error.response.data;
                            } else {
                                this.isError = true;
                                this.validationError = error.response.data;
                            }
                        } else if (error.request) {
                            console.error('No response received from the server:', error.request);
                            this.isError = true;
                            this.validationError = 'Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!';
                        } else {
                            console.error('Error setting up the request:', error.message);
                            this.isError = true;
                            this.validationError = 'Đã xảy ra lỗi khi xác thực. Vui lòng thử lại!';
                        }
                    }

                }
            }
        },
        async sendOtp() {

            try {
                const response = await axios.post(`auth/send-OTP/${this.phoneNumber}`);

                console.log(response);

                // Kiểm tra trạng thái phản hồi
                if (response.status === 200) {

                    this.toast.info(response.data.msg, { timeout: 3000 });

                } else {
                    console.error('Xác thực không thành công:', response.statusText);
                    this.isError = true;
                    this.validationError = response.data.msg;
                }
            } catch (error) {
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
                    this.validationError = 'Đã xảy ra lỗi khi gửi lại OTP. Vui lòng thử lại!';
                }
            }
        }
    }
};
</script>

<style scoped lang = "scss">
.hovered {
    cursor: pointer;
    color: #235dc7;
}

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

        .password-input {
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

            .icon-password {
                display: flex;
                align-items: center;
                margin-right: 4px;
                margin-left: 14px;
            }
        }
    }

    .otp {

        position: relative;
        margin-bottom: 23px;

        .otp-input {
            display: flex;

            .resend-otp {
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
                background-color: #61c5e7;
                /* Light sea blue color */
                transition: background-color 0.3s ease;
                /* Add a smooth transition effect */
            }

            .resend-otp:hover {
                background-color: #5f9ea0;
                /* Darker shade on hover */
            }

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
</style>