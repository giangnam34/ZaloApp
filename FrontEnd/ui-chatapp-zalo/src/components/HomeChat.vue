<template>
  <div id="homeChat">
    <div id="header">
      <div id="inforChat">
        <div id="avatarChatBox">
          <img src="https://s120-ava-talk.zadn.vn/3/e/6/b/1/120/8b72a04be66a9ef7009df15d86b69810.jpg" class="a-child">
        </div>
        <div id="titleChat">
          <div id="mainTitle">
              GiangNam
          </div>
          <div id="activity">

          </div>
        </div>
      </div>
      <div id="iconChat">

      </div>
    </div>
  </div>
</template>
  
<script>
export default {
  name: 'HomeChat'
}
</script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang = "scss">
#homeChat {
  display: inline-block;
  height: 100vh;
  width: 73%;

  #header {
    height: 68px;
    width: 100%;
    border-bottom: 1px solid rgb(160, 160, 160);

    #inforChat {
      padding-left: 16px;

      #avatarChatBox {
        //display: inline-block;
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

      #titleChat{
        display: inline-block;
      }
    }
  }
}
</style>
  