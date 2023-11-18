<template>
  <div id="sidebarNav">
    <div id="chat-message">
      <div id="search-content">
        <font-awesome-icon icon="fa-solid fa-magnifying-glass" />
        <input type="text" id="contact-input-search" placeholder="Tìm kiếm">
        <div title="Thêm bạn">
          <font-awesome-icon icon="fa-solid fa-user-plus" />
        </div>
        <div title="Tạo nhóm chat">
          <font-awesome-icon icon="fa-solid fa-users" />
        </div>
      </div>
      <div id="todo">
        <div class="header-container">
          <div class="title">
            <div class="todo-text">To-Do</div>
            <a id="icon-plus" title="Thêm việc">
              <font-awesome-icon icon="fa-solid fa-plus" />
            </a>
          </div>
          <div class="flex-space" style="flex: 1;"></div>
          <a id="icon-search" title="Tìm kiếm">
            <font-awesome-icon icon="fa-solid fa-magnifying-glass" />
          </a>
          <a id="icon-statitic" title="Thống kê">
            <font-awesome-icon icon="fa-solid fa-chart-simple" />
          </a>
        </div>
        <div style="display: flex; flex-direction: column;">
          <div style="width: 100%">
            <div class="tab1" style="justify-content: center; overflow: hidden;">
              <div class="item-tab active-item" title="Tôi giao">
                <span>Tôi giao</span>
                <div class="count">
                  3
                </div>
              </div>
              <div class="item-tab" title="Cần làm">
                <span>Cần làm</span>
                <div class="count">
                  3
                </div>
              </div>
              <div class="item-tab" title="Theo dõi">
                <span>Theo dõi</span>
              </div>
            </div>
            <div class="tab2">
              <div class="sub-tab">
                <span class="active-sub">
                  <span>Chưa xong:</span>
                  3
                </span>
              </div>
              <div class="sub-tab">
                <span>
                  <span>Đã xong:</span>
                  0
                </span>
              </div>
              <div class="sub-tab">
                <span>Khác</span>
                <a id="icon-down">
                  <font-awesome-icon icon="fa-solid fa-chevron-down" />
                </a>
              </div>
            </div>
          </div>
        </div>
        <div class="todo-list-content">
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <div class="item">
              <div class="avatar-container">
                <div class="avatar-wrapper">
                  <img src="https://i.imgur.com/gEKsypv.jpg" class="avatar">
                </div>
              </div>
              <div class="detail">
                <div class="title">
                  <span>Aaaaaaaa</span>
                </div>
                <div class="content">
                  <span>Aaaaaaaaaa</span>
                </div>
                <div class="status">
                  <span>Đã nhận</span>
                </div>
              </div>
            </div>
            <div class="time">
              <span>Vài giây</span>
            </div>
          </div>
          <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <div class="item">
              <div class="avatar-container">
                <div class="avatar-wrapper">
                  <img src="https://i.imgur.com/gEKsypv.jpg" class="avatar">
                </div>
              </div>
              <div class="detail">
                <div class="title">
                  <span>Aaaaaaaa</span>
                </div>
                <div class="content">
                  <span>Aaaaaaaaaa</span>
                </div>
                <div class="status">
                  <span>Đã nhận</span>
                </div>
              </div>
            </div>
            <div class="time">
              <span>Vài giây</span>
            </div>
          </div>
          <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <div class="item">
              <div class="avatar-container">
                <div class="avatar-wrapper">
                  <img src="https://i.imgur.com/gEKsypv.jpg" class="avatar">
                </div>
              </div>
              <div class="detail">
                <div class="title">
                  <span>Aaaaaaaa</span>
                </div>
                <div class="content">
                  <span>Aaaaaaaaaa</span>
                </div>
                <div class="status">
                  <span>Đã nhận</span>
                </div>
              </div>
            </div>
            <div class="time">
              <span>Vài giây</span>
            </div>
          </div>
          <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ContactNav'
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang = "scss">
::placeholder {
  //padding-left: 18px;
  font-size: 14px;
}

#sidebarNav {
  height: 100vh;
  width: 23%;
  border-right: 1px solid rgb(160, 160, 160);
  display: inline-block;

  #chat-message {
    display: inline-block;
    height: 100%;
    width: 100%;
    vertical-align: top;

    //background-color: black;

    #search-content {
      height: 64px;
      width: 100%;
      padding: 0 16px;
      display: flex;
      //background-color: black;

      #contact-input-search {
        height: 45%;
        width: 100%;
        margin-top: 18px;
        border-radius: 6px;
        background-color: #EAEDF0;
        padding-left: 18px;
      }

      #contact-input-search:focus {
        outline-color: #80b3ff;
        outline-width: 0.05px;
      }

      .fa-magnifying-glass {
        position: absolute;
        margin-left: 2px;
        margin-top: 26px;
        font-size: 12px;
      }

      .fa-user-plus,
      .fa-users {
        font-size: 14px;
        padding-left: 10px;
        margin-top: 26px;
      }
    }

    #todo {
      background-color: #f4f4f4;

      #icon-down {
        margin-left: 4px;
      }

      .count {
        margin-left: 8px;
      }

      .header-container {
        display: flex;
        align-items: center;
        background-color: #f4f4f4;
        height: 44px;

        .title {
          display: flex;
          align-items: center;
        }

        .todo-text {
          cursor: pointer;
          margin-top: 8px;
          margin-right: 8px;
          margin-left: 15px;
        }

        #icon-plus,
        #icon-search,
        #icon-statitic {
          margin-top: 8px;
          cursor: pointer;
          margin-left: 8px;
        }

        #icon-statitic,
        #icon-search {
          margin-right: 20px;
        }
      }

      .tab1,
      .tab2 {
        display: flex;
        justify-content: center;
        height: 44px;
        overflow: hidden;

        .item-tab {
          display: flex;
          align-items: center;
          justify-content: center;
          background-color: #f4f4f4;
          flex: 1;
          position: relative;
          border-bottom: 1px solid #ccc;
        }

        .active-item {
          background-color: #ffff;
          border-radius: 30% 30% 0 0;
          border-top: 1px solid #ccc;
          border-left: 1px solid #ccc;
          border-right: 1px solid #ccc;
          border-bottom: none;
        }

        &:not(:last-child) {
          margin-right: 8px;
        }

        .sub-tab {
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 12px;
          background-color: #ffffff;
          flex: 1;
        }

        .active-sub {
          background-color: #bde6f2;
          border-radius: 30px;
          font-size: 12px;
          text-align: center;
          padding: 6px;
          color: #2b43c6;
        }

        .active-sub span {
          color: #2b43c6;
        }
      }

      .tab2 {
        border-bottom: 1px solid #ccc;
      }

      .todo-list-content {
        background-color: #ffffff;

        .avatar-container {
          height: 80px;
          display: flex;
          align-items: center;
          margin-left: 14px;
        }

        .avatar-wrapper {
          width: 50px;
          height: 50px;
          display: flex;
          justify-content: center;
          align-items: center;
          overflow: hidden;
          border-radius: 50%;
          margin-right: 16px;
        }

        .avatar {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }

        .detail {
          display: flex;
          flex-direction: column;
          justify-content: center;
        }

        .item {
          display: flex;
          align-items: center;
        }

        .title,
        .content,
        .status {
          display: flex;
        }

        .title {
          font-style: bold;
        }

        .content,
        .status span {
          margin-top: 5px;
          margin-bottom: 2px;
          font-size: 13px;
        }

        .time{
          font-size: 12px;
          margin-right: 8px;
          margin-bottom: 55px;
        }
      }

    }

  }

}
</style>