<template>
    <div class="limiter">
        <div class="container">
            <div class="wrap-signin">
                <div class="validate-form">
                    <span class="title">Đăng ký</span>
                    <div class="name">
                        <span class="label-input">Tên</span>
                        <div class="name-input">
                            <div class="icon-user">
                                <font-awesome-icon icon="fa-solid fa-user" />
                            </div>
                            <input class="input" type="text" v-model="username" name="name"
                                placeholder="Nhập tên người dùng" @blur="validateName" ref="nameInput" required>
                        </div>
                        <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                    </div>
                    <div class="phone">
                        <span class="label-input">Số điện thoại</span>
                        <div class="phone-input">
                            <div class="icon-phone">
                                <font-awesome-icon icon="fa-solid fa-phone" />
                            </div>
                            <input class="input" type="text" v-model="phoneNumber" name="phone"
                                placeholder="Nhập số điện thoại" @blur="validatePhoneNumber" ref="phoneInput" required>
                        </div>
                        <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                    </div>
                    <div class="password">
                        <span class="label-input">Mật khẩu</span>
                        <div class="password-input">
                            <div class="icon-password">
                                <font-awesome-icon icon="fa-solid fa-key" />
                            </div>
                            <input class="input" type="password" v-model="password" name="password"
                                placeholder="Nhập mật khẩu" @blur="validatePassword" ref="passwordInput" required>
                            <button class="toggle-password" @click="togglePasswordVisibility">
                                <font-awesome-icon :icon="showPassword ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'" />
                            </button>
                        </div>
                        <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                    </div>
                    <div class="password">
                        <span class="label-input">Xác nhận mật khẩu</span>
                        <div class="password-input">
                            <div class="icon-password">
                                <font-awesome-icon icon="fa-solid fa-key" />
                            </div>
                            <input class="input" type="password" v-model="confirmPassword" name="confirmPassword"
                                placeholder="Nhập lại mật khẩu" @blur="validateConfirmPassword" ref="confirmPasswordInput"
                                required>
                            <button class="toggle-password" @click="togglePasswordVisibility">
                                <font-awesome-icon :icon="showPassword ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'" />
                            </button>
                        </div>
                        <hr style="border: none; border-bottom: 2px solid #d9d9d9; margin-left: 8px;">
                    </div>
                    <em class="error" v-if="isError === true">{{ validationError }}</em>
                    <div class="container-signup-button">
                        <div class="wrap-signup-button">
                            <div class="signup-bgbutton"></div>
                            <button class="signup-button"
                                @click="signUp">Đăng ký</button>
                        </div>
                    </div>
                    <div class="signin">
                        <span class="signin-title">Đã có tài khoản?</span>
                        <span class="signin-link" :class="{ 'hovered': hoveredItem === 'signin' }"
                            @mouseenter="(hoveredItem = 'signin')" @mouseleave="hoveredItem = ''" @click="showSignIn">Đăng
                            nhập</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            showPassword: false,
            isError: false,
            hoveredItem: '',
            username: '',
            phoneNumber: '',
            password: '',
            confirmPassword: '',
            validationError: '',
            flag1: true,
            flag2: true,
            flag3: true,
            flag4: true,
        };
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
            const phoneNumber = this.phoneNumber;
            const isValidPhoneNumber = /^0\d{9}$/.test(phoneNumber);

            if (!isValidPhoneNumber) {
                this.isError = true;
                this.validationError = 'Số điện thoại không hợp lệ!';
            } else {
                this.isError = false;
                this.validationError = '';
                this.flag2 = false;
            }
        },
        validatePassword() {
            const password = this.password;
            const confirmPassword = this.confirmPassword;
            if (password.length == 0) {
                this.isError = true;
                this.validationError = 'Không được để trống mật khẩu!';
            } else {
                this.isError = false;
                this.validationError = '';
            }
            if ((password == confirmPassword) && confirmPassword.length != 0) {
                this.error = false;
                this.validationError = '';
                this.flag3 = false;
            }else{
                this.flag3 = true;
                this.flag4 = true;
            }
        },
        validateConfirmPassword() {
            const password = this.password;
            const confirmPassword = this.confirmPassword;
            if (confirmPassword.length == 0) {
                this.isError = true;
                this.validationError = 'Không được để trống xác nhận mật khẩu!';
            }
            if (!(password == confirmPassword)) {
                this.isError = true;
                this.validationError = 'Mật khẩu và xác nhận mật khẩu không khớp!';
                this.flag3 = true;
                this.flag4 = true;
            } else {
                this.error = false;
                this.validationError = '';
                this.flag4 = false;
            }
        },
        validateName() {
            const username = this.username;
            const regex = /^([A-ZĐÀÁẢÃẠĂẰẮẲẴẶÂẦẤẨẪẬÈÉẺẼẸÊỀẾỂỄỆÌÍỈĨỊÒÓỎÕỌÔỒỐỔỖỘƠỜỚỞỠỢÙÚỦŨỤƯỪỨỬỮỰỲÝỶỸỴĐ][a-zđàáảãạăằắẳẵặâầấẩẫậèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵ\s]*)+$/u;
            if (username.length == 0) {
                this.isError = true;
                this.validationError = 'Không được để trống tên người dùng!';
            } else if (!regex.test(username)) {
                this.isError = true;
                this.validationError = 'Tên người dùng không hợp lệ!';
            } else {
                this.isError = false;
                this.validationError = '';
                this.flag1 = false;
            }
        },
        showSignIn() {
            this.$emit('update:showingPage', 'signIn');
        },
        signUp(){
            if(this.flag1 || this.flag2 || (this.flag3 && this.flag4)){
                this.isError = true;
                this.validationError = "Vui lòng nhập đầy đủ các thông tin!";
            }
        }
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
    }
}
</style>