// { "framework": "Vue"} 

!function(e){function t(n){if(r[n])return r[n].exports;var o=r[n]={i:n,l:!1,exports:{}};return e[n].call(o.exports,o,o.exports,t),o.l=!0,o.exports}var r={};t.m=e,t.c=r,t.i=function(e){return e},t.d=function(e,r,n){t.o(e,r)||Object.defineProperty(e,r,{configurable:!1,enumerable:!0,get:n})},t.n=function(e){var r=e&&e.__esModule?function(){return e.default}:function(){return e};return t.d(r,"a",r),r},t.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},t.p="",t(t.s=24)}({1:function(e,t,r){"use strict";function n(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}Object.defineProperty(t,"__esModule",{value:!0});var o=function(){function e(e,t){for(var r=0;r<t.length;r++){var n=t[r];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,r,n){return r&&e(t.prototype,r),n&&e(t,n),t}}(),i=weex.requireModule("dialog"),s=function(){function e(){n(this,e)}return o(e,null,[{key:"showTwoBtnAlertDialog",value:function(e,t,r,n,o){i.showTwoBtnAlertDialog(e,t,r,n,o)}}]),e}();t.default=s},13:function(e,t){e.exports={timertext:{marginLeft:30,fontSize:26,color:"#0088fb",fontWeight:"bold"},panel:{width:600,height:100,marginLeft:10,marginTop:8,marginRight:10,flexDirection:"column",justifyContent:"center",borderWidth:2,borderStyle:"solid",borderColor:"#afddff",background:"azure"}}},19:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",[r("text",[e._v("已注册用户")]),r("list",e._l(e.lists,function(t,n){return r("cell",{staticClass:["cell"],appendAsTree:!0,attrs:{append:"tree"}},[r("div",{staticClass:["panel"],on:{click:function(t){e.deleteUser(n)}}},[r("text",{staticClass:["timertext"]},[e._v("昵称")]),r("text",{staticClass:["timertext"]},[e._v(e._s(t.userNickname))])])])}))])},staticRenderFns:[]},e.exports.render._withStripped=!0},24:function(e,t,r){var n,o,i=[];i.push(r(13)),n=r(6);var s=r(19);o=n=n||{},"object"!=typeof n.default&&"function"!=typeof n.default||(Object.keys(n).some(function(e){return"default"!==e&&"__esModule"!==e})&&console.error("named exports are not supported in *.vue files."),o=n=n.default),"function"==typeof o&&(o=o.options),o.__file="/Users/qiao/File/work/homevue/src/showuser.vue",o.render=s.render,o.staticRenderFns=s.staticRenderFns,o._scopeId="data-v-3402b4e7",o.style=o.style||{},i.forEach(function(e){for(var t in e)o.style[t]=e[t]}),"function"==typeof __register_static_styles__&&__register_static_styles__(o._scopeId,i),e.exports=n,e.exports.el="true",new Vue(e.exports)},6:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=r(1),o=function(e){return e&&e.__esModule?e:{default:e}}(n);t.default={name:"showuser",data:{lists:[{userNickname:"当前没有用户"}]},mounted:function(){var e=this;weex.requireModule("businessLauncher").getUserList(function(t){e.lists=JSON.parse(t)})},methods:{deleteUser:function(e){var t=this;console.log("itemclick"+e),o.default.showTwoBtnAlertDialog("删除该用户","删除该用户的信息","不删除","删除",function(r){if(r.res="right"){weex.requireModule("businessLauncher").deleteUser(t.lists[e].userId),t.lists.splice(e,1)}})}}}}});