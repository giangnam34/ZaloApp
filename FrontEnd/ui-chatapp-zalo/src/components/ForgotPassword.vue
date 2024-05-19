<template>
    <div class="limiter">
        <div class="container">
            <div class="wrap-forgotPassword" v-if="!showOTP">
                <div class="validate-form">
                    <span class="title">Quên mật khẩu</span>
                    <div class="phone">
                        <span class="label-input">Số điện thoại</span>
                        <div class="phone-input">
                            <div class="icon-phone">
                                <font-awesome-icon icon="fa-solid fa-phone" />
                            </div>
                            <input class="input" type="number" v-model="phoneNumber" name="phone"
                                placeholder="Nhập số điện thoại" @blur="validatePhoneNumber" ref="phoneInput" required
                                @keyup.enter="confirm">
                        </div>
                        <em class="error" v-if="isError === true">{{ validationErrorPhoneNumber }}</em>
                        <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                    </div>
                    <em class="error" v-if="isError === true">{{ validationError }}</em>
                    <div class="container-forgotPassword-button">
                        <div class="wrap-forgotPassword-button">
                            <div class="forgotPassword-bgbutton"></div>
                            <button class="forgotPassword-button" @click="confirm">Xác nhận</button>
                        </div>
                    </div>
                    <div class="back-to-signin">
                        <span class="signin-link" :class="{ 'hovered': hoveredItem === 'signin' }"
                            @mouseenter="(hoveredItem = 'signin')" @mouseleave="hoveredItem = ''" @click="showSignIn">Quay
                            lại đăng nhập</span>
                    </div>
                </div>
            </div>

            <div class="wrap-otp" v-if="showOTP">
                <OTPForgotPassword style="width: 100%;" @update:showingPage="showSignIn" @update:showOTP="updateShowOTP"
                    :phoneNumber="phoneNumber">
                </OTPForgotPassword>
            </div>
        </div>
    </div>
</template>

<script>

import axios from 'axios';
import { useToast } from "vue-toastification";
import OTPForgotPassword from './OTPForgotPassword.vue';

export default {
    data() {
        return {
            isError: true,
            hoveredItem: '',
            phoneNumber: '',
            validationErrorPhoneNumber: '',
            validationError: '',
            registerUser: null,
            showOTP: false,
            flag1: true,
        };
    },
    components: {
        OTPForgotPassword
    },
    setup() {
        // Get toast interface
        const toast = useToast();
        return { toast}
    },
    methods: {
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
        validatePhoneNumber() {
            this.phoneNumber = this.phoneNumber + '';
            if (this.phoneNumber.length == 9 && this.phoneNumber.at(0) != '0') {
                this.phoneNumber = '0' + this.phoneNumber;
            }
            const phoneNumber = this.phoneNumber;
            const isValidPhoneNumber = /^0\d{9}$/.test(phoneNumber);

            if (!isValidPhoneNumber) {
                this.isError = true;
                this.validationErrorPhoneNumber = 'Số điện thoại không hợp lệ!';
                this.flag1 = true;
            } else {
                this.validationErrorPhoneNumber = '';
                this.flag1 = false;
            }
            if (!this.flag1) this.isError = false;
        },
        showSignIn() {
            this.$emit('update:showingPage', 'signIn');
        },
        updateShowOTP(value) {
            this.showOTP = value;
        },
        async confirm() {

            if (this.phoneNumber == '') {
                this.isError = true;
                this.validationError = "Vui lòng nhập số điện thoại!"
            } else {

                this.validatePhoneNumber();

                if (!this.isError) {


                    await this.sendOtp();

                    if(!this.isError){
                        this.showOTP = true;
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
                    this.validationError = 'Đã xảy ra lỗi khi gửi lại OTP. Vui lòng thử lại!';
                }
            }
        },
        handleEnterKey(event) {
            if (event.key === 'Enter') {
                this.confirm();
            }
        },
    }
};
</script>

<style scoped lang = "scss">
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
        min-height: 100vh;
        flex-wrap: wrap;
        justify-content: center;
        align-items: center;
        padding: 15px;
        background-image: url('../assets/img/bg-01.jpg');
        background-size: cover;

        .wrap-forgotPassword {
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

                .phone {

                    position: relative;
                    margin-bottom: 23px;

                    .phone-input {
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

                        .icon-phone {
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
                }

                .container-forgotPassword-button {
                    display: flex;
                    flex-wrap: wrap;
                    justify-content: center;
                    margin-bottom: 23px;

                    .wrap-forgotPassword-button {
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

                        .forgotPassword-bgbutton {
                            position: absolute;
                            z-index: -1;
                            width: 300%;
                            height: 100%;
                            background: #a64bf4;
                            background: linear-gradient(to right, #00dbde, #fc00ff, #00dbde, #fc00ff);
                            left: -100%;
                            transition: all .4s;
                        }

                        .forgotPassword-button {
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

                .back-to-signin {
                    display: flex;
                    flex-direction: column;
                    align-items: center;

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