<template>
  <div id="homeChat">
    <div id="header">
      <div id="inforChat">
        <div id="avatarChatBox">
          <img src="https://s120-ava-talk.zadn.vn/3/e/6/b/1/120/8b72a04be66a9ef7009df15d86b69810.jpg" class="a-child">
        </div>
        <div id="titleChat">
          <div id="mainTitle">
            <b id="userName">Giang Nam</b>
            <font-awesome-icon icon="fa-solid fa-pencil" title="Chỉnh sửa" />
            <div id="header-subtitle">
              <span id="lastSeen">Truy cập 1 giờ trước</span>
              <div class="new-separator"></div>
              <font-awesome-icon icon="fa-solid fa-tag" title="Phân loại" style="color: #a5a9b1;" />
            </div>
          </div>
        </div>
      </div>
      <div id="iconChat">
        <div><font-awesome-icon icon="fa-solid fa-user-plus" title="Thêm bạn vào trò chuyện" /></div>
        <div><font-awesome-icon icon="fa-solid fa-magnifying-glass" title="Tìm kiếm tin nhắn" /></div>
        <div><font-awesome-icon icon="fa-solid fa-video" title="Cuộc gọi video" /></div>
        <div><font-awesome-icon icon="fa-solid fa-info" title="Thông tin hội thoại" /></div>
      </div>
    </div>
    <article id="chatView">
      <div id="messageView">

      </div>
      <div class="chat-input-v2">
        <div>
          <font-awesome-icon icon="fa-regular fa-note-sticky" title="Gửi sticker" />
        </div>
        <div>
          <font-awesome-icon icon="fa-regular fa-image" title="Gửi hình ảnh" />
        </div>
        <div>
          <font-awesome-icon icon="fa-solid fa-paperclip" title="Đính kèm file" />
        </div>
        <div>
          <font-awesome-icon icon="fa-solid fa-camera-retro" title="Chụp màn hình(Windows + Shitf + S)" />
        </div>
        <div>
          <font-awesome-icon icon="fa-regular fa-address-card" title="Gửi danh thiếp" />
        </div>
        <div>
          <font-awesome-icon icon="fa-regular fa-clock" title="Tạo nhắc hẹn" />
        </div>
        <div>
          <font-awesome-icon icon="fa-regular fa-square-check" title="Giao việc" />
        </div>
        <div>
          <font-awesome-icon icon="fa-solid fa-font" title="Định dạng tin nhắn" />
        </div>
        <div>
          <font-awesome-icon icon="fa-solid fa-exclamation" title="Tin nhắn ưu tiên" />
        </div>
        <div>
          <font-awesome-icon icon="fa-solid fa-ellipsis" />
        </div>
      </div>

      <div id="chatInput">
        <textarea id="richInput" placeholder="Nhập @, tin nhắn tới Giang Nam"></textarea>
        <div id="chat-input-btns">
          <div>          
            <font-awesome-icon icon="fa-brands fa-rocketchat" />
          </div>
          <div>
            <font-awesome-icon icon="fa-regular fa-face-laugh" />
          </div>
          <div>
            <font-awesome-icon icon="fa-solid fa-at" />
          </div>
          <div>
            <font-awesome-icon icon="fa-regular fa-thumbs-up" />
          </div>
        </div>
      </div>
    </article>
  </div>
</template>
  
<script>
export default {
  name: 'HomeChat',
  data() {
    return {
      hover : false
    }
  },
}
</script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang = "scss">
#homeChat {
  vertical-align: top;
  display: inline-block;
  height: 100vh;
  width: 73%;

  #header {
    height: 9.015vh;
    width: 100%;
    border-bottom: 1px solid rgb(160, 160, 160);

    #inforChat {
      padding-left: 16px;
      width: 300px;
      display: inline-block;

      #avatarChatBox {
        display: inline-block;
        height: 100%;
        width: 50px;
        padding-top: 12px;


        .a-child {
          width: 48px;
          height: 48px;
          display: flex;
          justify-content: center;
          align-items: center;
          background: var(--surface-background);
          color: var(--text-secondary);
          overflow: hidden;
          object-fit: contain;
          font-size: .75rem;
          font-weight: 500;
          line-height: 1.5;
          border: 1px solid var(--WA100);
          //box-sizing: border-box;
          border-radius: 50%;
          margin-left: 7px;
        }
      }

      #titleChat {
        display: inline-block;
        vertical-align: top;
        margin-top: 10px;
        margin-left: 15px;

        #mainTitle {
          #userName {
            color: #081c36;
            text-overflow: ellipsis;
            white-space: nowrap;
            height: 28px;
          }

          .fa-pencil {
            font-size: 14px;
            margin-left: 5px;
          }

          #header-subtitle {
            display: block;

            #lastSeen {
              font-size: .875rem;
              color: #7589a3;
            }

            .new-separator {
              display: inline-block;
              background: #d6dbe1;
              height: 12px;
              width: 2px;
              min-width: 1px;
              margin: 0 8px;
            }

            .fa-tag {
              font-size: 1.125rem;
              width: 1.125rem;
              height: 1.125rem;
            }
          }


        }
      }

    }

    #iconChat {
      display: inline-block;
      float: right;
      padding-top: 20px;
      width: 130px;

      div {
        width: 32px;
        display: inline-block;
        text-align: center;
      }
    }

  }

  #chatView {
    height: 90.985vh;

    #messageView {
      height: 77.045vh;
      flex: 1 0 auto;
      overflow: hidden;
      position: relative;
      display: flex;
      flex-direction: column;
      background-color: #b1b5b9;
    }

    .chat-input-v2 {
      height: 6.2vh;
      border-top: 1px solid d6dbe1;
      border-bottom: 1px solid rgb(160, 160, 160);
      

      div {
        width: 50px;
        height: 100%;
        display: inline-block;
        text-align: center;
        margin-top: 8px;
        font-size: 1.3rem;
      }

    }

    #chatInput {
      height: 7.74vh;
      #richInput{
        resize: none;
        border: none;
        outline: none;
        display: inline-block;
        width: 940px;
        height: 50%;
        margin: 15px;
      }
      #chat-input-btns{
        display: inline-block;
        vertical-align: top;
        

        div{
          display: inline-block;
          font-size: 1.3rem;
          margin-top: 14px;
          width: 34px;
          text-align: center;
        }
      }
    }
  }
}
</style>
  