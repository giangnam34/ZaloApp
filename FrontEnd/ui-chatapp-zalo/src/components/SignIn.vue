<template>
    <div v-if="showingPage === 'signIn'" style="width:100%">
        <div class="limiter">
            <div class="container">
                <div class="wrap-signin">
                    <div class="validate-form">
                        <span class="title">Đăng nhập</span>
                        <div class="phone">
                            <span class="label-input">Số điện thoại</span>
                            <div class="phone-input">
                                <div class="icon-phone">
                                    <font-awesome-icon icon="fa-solid fa-phone" />
                                </div>
                                <input class="input" type="text" v-model="phoneNumber" name="phone"
                                    placeholder="Nhập số điện thoại" @blur="validatePhoneNumber" ref="phoneInput" required
                                    @keyup.enter="signIn">
                            </div>
                            <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                            <em class="error" v-if="isError === true">{{ validationErrorPhoneNumber }}</em>
                        </div>
                        <div class="password" data-validate="Vui lòng nhập mật khẩu!">
                            <span class="label-input">Mật khẩu</span>
                            <div class="password-input">
                                <div class="icon-password">
                                    <font-awesome-icon icon="fa-solid fa-key" />
                                </div>
                                <input class="input" type="password" v-model="password" name="password"
                                    placeholder="Nhập mật khẩu" @blur="validatePassword" ref="passwordInput" required
                                    @keyup.enter="signIn">
                                <button class="toggle-password" @click="togglePasswordVisibility">
                                    <font-awesome-icon :icon="showPassword ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'" />
                                </button>
                            </div>
                            <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                        </div>
                        <em class="error" v-if="isError === true">{{ validationError }}</em>
                        <div class="forgot-password">
                            <span :class="{ 'hovered': hoveredItem === 'forgot-password' }"
                                @mouseenter="(hoveredItem = 'forgot-password')" @mouseleave="hoveredItem = ''"
                                @click="showForgotPassword">Quên mật
                                khẩu?</span>
                        </div>
                        <div class="container-signin-button">
                            <div class="wrap-signin-button">
                                <div class="sigin-bgbutton"></div>
                                <button class="signin-button" @click="signIn">Đăng nhập</button>
                            </div>
                        </div>
                        <div class="signup">
                            <span class="signup-title">Chưa có tài khoản?</span>
                            <span class="signup-link" :class="{ 'hovered': hoveredItem === 'signup' }"
                                @mouseenter="(hoveredItem = 'signup')" @mouseleave="hoveredItem = ''"
                                @click="showSignUp">Đăng ký</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div v-if="showingPage === 'signUp'" style="width: 100%;">
        <SignUp v-model="showingPage" @update:showingPage="updateShowingPage"></SignUp>
    </div>
    <div v-if="showingPage === 'forgotPassword'" style="width: 100%;">
        <ForgotPassword v-model="showingPage" @update:showingPage="updateShowingPage"></ForgotPassword>
    </div>
</template>

<script>
import SignUp from './SignUp.vue';
import ForgotPassword from './ForgotPassword.vue';
import axios from 'axios';
import { useToast } from "vue-toastification";
export default {
    data() {
        return {
            showingPage: 'signIn',
            showPassword: false,
            isError: false,
            hoveredItem: '',
            phoneNumber: '',
            password: '',
            validationErrorPhoneNumber: '',
            validationError: '',
            flag1: true,
            flag2: true,
        };
    },
    components: {
        SignUp,
        ForgotPassword
    },
    setup() {
        // Get toast interface
        const toast = useToast();
        return { toast }
    },
    emits: ['userLoggedIn'],
    methods: {
        togglePasswordVisibility() {
            this.showPassword = !this.showPassword;
            const passwordInput = document.querySelector('input[name="password"]');
            if (this.showPassword) {
                passwordInput.setAttribute('type', 'text');
            } else {
                passwordInput.setAttribute('type', 'password');
            }
        },
        validatePhoneNumber() {
            const phoneNumber = this.phoneNumber;
            const isValidPhoneNumber = /^0\d{9}$/.test(phoneNumber);

            if (phoneNumber.length && !isValidPhoneNumber) {
                this.isError = true;
                this.validationErrorPhoneNumber = 'Số điện thoại không hợp lệ!';
                this.flag1 = true;
            } else {
                this.validationErrorPhoneNumber = '';
                this.flag1 = false;
            }
            if (!this.flag1 && !this.flag2) this.isError = false;
        },
        validatePassword() {
            if (this.password.length > 0) {
                this.flag2 = false;
                this.validationError = '';
            }
            else this.flag2 = true;
            if (!this.flag1 && !this.flag2) this.isError = false;
        },
        showSignUp() {
            this.showingPage = 'signUp';
        },
        showForgotPassword() {
            this.showingPage = 'forgotPassword';
        },
        updateShowingPage(value) {
            this.showingPage = value;
        },
        async signIn() {
            if (this.phoneNumber == '' && this.password == '') {
                this.isError = true;
                this.validationError = "Vui lòng nhập đầy đủ số điện thoại và mật khẩu!"
            } else if (this.phoneNumber == '') {
                this.isError = true;
                this.validationError = "Vui lòng nhập số điện thoại!"
            } else if (this.password == '') {
                this.isError = true;
                this.validationError = "Vui lòng nhập mật khẩu!"
            } else {
                this.validatePhoneNumber();
                this.validatePassword();
                if (!this.isError) {
                    try {
                        const response = await axios.post('auth/login', {
                            userName: this.phoneNumber,
                            password: this.password
                        });

                        // Kiểm tra trạng thái phản hồi
                        if (response.status === 200) {
                            // Đăng nhập thành công

                            const jwtToken = response.data.jwt;

                            localStorage.setItem('token', jwtToken);

                            this.toast.success("Đăng nhập thành công!", { timeout: 1500 });

                            this.$emit("userLoggedIn", jwtToken);

                        } else {
                            console.error('Đăng nhập không thành công:', response.statusText);
                            this.isError = true;
                            this.validationError = 'Tên đăng nhập hoặc mật khẩu không đúng! Vui lòng nhập lại!';
                        }
                    } catch (error) {
                        if (error.response) {
                            console.error('Server responded with an error status:', error.response.status);

                            if (error.response.status === 400) {
                                this.isError = true;
                                this.validationError = 'Xảy ra lỗi trong quá trình đăng nhập!';
                            } else {
                                this.isError = true;
                                this.validationError = 'Tên đăng nhập hoặc mật khẩu không đúng! Vui lòng nhập lại!';
                            }
                        } else if (error.request) {
                            console.error('No response received from the server:', error.request);
                            this.isError = true;
                            this.validationError = 'Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!';
                        } else {
                            console.error('Error setting up the request:', error.message);
                            this.isError = true;
                            this.validationError = 'Đã xảy ra lỗi khi đăng nhập. Vui lòng thử lại!';
                        }
                    }
                }
            }

        },
        handleEnterKey(event) {
            if (event.key === 'Enter') {
                this.signIn();
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

        .wrap-signin {
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

                .container-signin-button {
                    display: flex;
                    flex-wrap: wrap;
                    justify-content: center;
                    margin-bottom: 23px;

                    .wrap-signin-button {
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

                        .signin-bgbutton {
                            position: absolute;
                            z-index: -1;
                            width: 300%;
                            height: 100%;
                            background: #a64bf4;
                            background: linear-gradient(to right, #00dbde, #fc00ff, #00dbde, #fc00ff);
                            left: -100%;
                            transition: all .4s;
                        }

                        .signin-button {
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

                .signup {
                    display: flex;
                    flex-direction: column;
                    align-items: center;

                    .signup-title {
                        font-family: Poppins-Regular;
                        font-size: 18px;
                        line-height: 1.5;
                        color: #666;
                        margin-bottom: 2px;
                    }

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