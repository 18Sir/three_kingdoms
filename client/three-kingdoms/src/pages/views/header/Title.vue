<template>
        <ul :class="titleStyle" >
            <li v-for="(item,index) in titles" :key="index" class="title-item">
                {{item}}
            </li>
        </ul>
</template>

<script>
    export default {
        data(){
            return{
                fontSize:20
            }
        },
        mounted() {

        },
        props:{
            titles:{
                type:Array
            },
            titleStyle:{
                type:String
            }
        }
    }
</script>

<style scoped>
    .title-box{
        list-style-type: none;
        width: 40%;
        height: 8vh;
        line-height: 8vh;
        font-size: 1.5vw;
        display: flex;
        align-content: center;
        justify-content: space-around;
    }
    .left-title{
        width: 40%;
    }
    .right-title{
        width: 20%;
    }
    .title-item{
        width: 20%;
        text-align: center;
        float: left;
        box-sizing: border-box;
        /*background-color: antiquewhite;*/
    }
    .title-item:hover{
        cursor: pointer;
        border-bottom: 3px solid black;
        color: black;
        font-weight: bold;
    }
</style>