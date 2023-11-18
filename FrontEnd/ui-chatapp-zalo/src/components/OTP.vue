<template>
    <div class="validate-form">
        <span class="title">Nhập OTP</span>
        <div class="otp">
            <span class="label-input">OTP</span>
            <div class="otp-input">
                <div class="icon-otp">
                    <font-awesome-icon icon="fa-solid fa-unlock-keyhole" />
                </div>
                <input class="input" type="text" v-model="otp" name="otp" placeholder="Nhập OTP" required
                    @keyup.enter="confirm" @blur="validateOTP" />
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

// import axios from 'axios';

export default {
    data() {
        return {
            isError: true,
            hoveredItem: '',
            validationError: '',
            otp: '',
        };
    },
    props: ['phoneNumber'],
    methods: {
        showSignIn() {
            this.$emit('update:showingPage', 'signIn');
        },
        showSignUp() {
            this.$emit('update:showOTP', false);
        },
        async signUp() {

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
                    this.showOTP = true;
                }
            }
        },
        handleEnterKey(event) {
            if (event.key === 'Enter') {
                this.confirm();
            }
        },
        confirm() {

        },
        sendOtp() {

        },
        validateOTP() {
            const otpRegex = /^\d{6}$/;

            if (!this.otp.match(otpRegex)) {
                this.isError = true;
                this.validationError = "Mã OTP không hợp lệ!";
            } else {
                this.isError = false;
                this.validationError = "";
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