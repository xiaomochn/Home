// { "framework": "Vue"} 

!function(e){function t(r){if(n[r])return n[r].exports;var o=n[r]={i:r,l:!1,exports:{}};return e[r].call(o.exports,o,o.exports,t),o.l=!0,o.exports}var n={};t.m=e,t.c=n,t.i=function(e){return e},t.d=function(e,n,r){t.o(e,n)||Object.defineProperty(e,n,{configurable:!1,enumerable:!0,get:r})},t.n=function(e){var n=e&&e.__esModule?function(){return e.default}:function(){return e};return t.d(n,"a",n),n},t.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},t.p="",t(t.s=22)}({0:function(e,t,n){"use strict";function r(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}Object.defineProperty(t,"__esModule",{value:!0});var o=function(){function e(e,t){for(var n=0;n<t.length;n++){var r=t[n];r.enumerable=r.enumerable||!1,r.configurable=!0,"value"in r&&(r.writable=!0),Object.defineProperty(e,r.key,r)}}return function(t,n,r){return n&&e(t.prototype,n),r&&e(t,r),t}}(),a=function(){function e(){r(this,e)}return o(e,null,[{key:"getttt",value:function(){var e=Date.now();return{"X-APICloud-AppId":"A6970328512280","X-APICloud-AppKey":this.hex_sha1("A6970328512280UZA411EB7A-E27D-E60F-EB23-F893EF978E06UZ"+e)+"."+e,"Content-Type":"application/json"}}},{key:"hex_sha1",value:function(e){return this.binb2hex(this.core_sha1(this.AlignSHA1(e)))}},{key:"core_sha1",value:function(e){for(var t=e,n=Array(80),r=1732584193,o=-271733879,a=-1732584194,i=271733878,s=-1009589776,u=0;u<t.length;u+=16){for(var c=r,l=o,d=a,f=i,g=s,h=0;h<80;h++){n[h]=h<16?t[u+h]:this.rol(n[h-3]^n[h-8]^n[h-14]^n[h-16],1);var p=this.safe_add(this.safe_add(this.rol(r,5),this.sha1_ft(h,o,a,i)),this.safe_add(this.safe_add(s,n[h]),this.sha1_kt(h)));s=i,i=a,a=this.rol(o,30),o=r,r=p}r=this.safe_add(r,c),o=this.safe_add(o,l),a=this.safe_add(a,d),i=this.safe_add(i,f),s=this.safe_add(s,g)}return new Array(r,o,a,i,s)}},{key:"sha1_ft",value:function(e,t,n,r){return e<20?t&n|~t&r:e<40?t^n^r:e<60?t&n|t&r|n&r:t^n^r}},{key:"sha1_kt",value:function(e){return e<20?1518500249:e<40?1859775393:e<60?-1894007588:-899497514}},{key:"safe_add",value:function(e,t){var n=(65535&e)+(65535&t);return(e>>16)+(t>>16)+(n>>16)<<16|65535&n}},{key:"rol",value:function(e,t){return e<<t|e>>>32-t}},{key:"AlignSHA1",value:function(e){for(var t=1+(e.length+8>>6),n=new Array(16*t),r=0;r<16*t;r++)n[r]=0;for(r=0;r<e.length;r++)n[r>>2]|=e.charCodeAt(r)<<24-8*(3&r);return n[r>>2]|=128<<24-8*(3&r),n[16*t-1]=8*e.length,n}},{key:"binb2hex",value:function(e){for(var t="0123456789abcdef",n="",r=0;r<4*e.length;r++)n+=t.charAt(e[r>>2]>>8*(3-r%4)+4&15)+t.charAt(e[r>>2]>>8*(3-r%4)&15);return n}},{key:"calcDigest",value:function(){var e=hex_sha1(document.SHAForm.SourceMessage.value);document.SHAForm.MessageDigest.value=e}}]),e}();t.default=a},12:function(e,t){e.exports={wrapper:{alignItems:"center",marginTop:120},registerc:{marginTop:120},title:{paddingTop:40,paddingBottom:40,fontSize:48},logo:{width:360,height:156},desc:{paddingTop:20,color:"#888888",fontSize:24}}},18:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:["wrapper"]},[n("image",{staticClass:["logo"],attrs:{src:e.logoUrl},on:{click:e.tohome}}),n("text",{staticClass:["logo"],on:{click:e.update}},[e._v("点击注册微信用户")]),n("text",{staticClass:["title"],on:{click:e.update}},[e._v("查看已注册用户")]),n("text",{staticClass:["desc"]},[e._v("下面 没注明作用的  都是测试按钮 莫管")]),n("text",{on:{click:e.totest}},[e._v(" totest")]),e.notregisted?n("text",{staticClass:["registerc"],on:{click:e.register}},[e._v(" register")]):e._e(),n("text",{staticClass:["registerc"],on:{click:e.callwxjs}},[e._v(" callwxjs")])])},staticRenderFns:[]},e.exports.render._withStripped=!0},2:function(e,t,n){"use strict";function r(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}Object.defineProperty(t,"__esModule",{value:!0});var o=function(){function e(e,t){for(var n=0;n<t.length;n++){var r=t[n];r.enumerable=r.enumerable||!1,r.configurable=!0,"value"in r&&(r.writable=!0),Object.defineProperty(e,r.key,r)}}return function(t,n,r){return n&&e(t.prototype,n),r&&e(t,r),t}}(),a=weex.requireModule("businessLauncher"),i=function(){function e(){r(this,e)}return o(e,null,[{key:"getDeviceId",value:function(e){a.getDeviceId(e)}},{key:"getString",value:function(e,t){a.getString(t,e)}},{key:"setString",value:function(e,t){a.setString(e,t)}},{key:"getObjectString",value:function(e,t){a.getString(function(e){t(JSON.parse(e))},e)}},{key:"setObject",value:function(e,t){a.setString(e,JSON.stringify(t))}},{key:"getDeviceName",value:function(e){a.getDeviceName(e)}},{key:"sendMessageToid",value:function(e,t){a.sendMessageToid(e,t)}}]),e}();t.default=i},22:function(e,t,n){var r,o,a=[];a.push(n(12)),r=n(4);var i=n(18);o=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(Object.keys(r).some(function(e){return"default"!==e&&"__esModule"!==e})&&console.error("named exports are not supported in *.vue files."),o=r=r.default),"function"==typeof o&&(o=o.options),o.__file="/Users/qiao/File/work/homevue/src/index.vue",o.render=i.render,o.staticRenderFns=i.staticRenderFns,o._scopeId="data-v-47cad1c3",o.style=o.style||{},a.forEach(function(e){for(var t in e)o.style[t]=e[t]}),"function"==typeof __register_static_styles__&&__register_static_styles__(o._scopeId,a),e.exports=r,e.exports.el="true",new Vue(e.exports)},4:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var o=n(0),a=r(o),i=n(2),s=r(i),u=weex.requireModule("stream"),c=weex.requireModule("modal");t.default={data:{logoUrl:"http://img1.vued.vanthink.cn/vued08aa73a9ab65dcbd360ec54659ada97c.png",target:"World",notregisted:!1},mounted:function(){var e=this;s.default.getDeviceId(function(t){u.fetch({method:"GET",url:'https://d.apicloud.com/mcm/api/device?filter={"where":{"did":"'+t+'"},"limit":1}',type:"json",headers:a.default.getttt()},function(t){console.log("11111111"+JSON.stringify(t.data)),t.ok&&(t.data.length<1?e.notregisted=!0:s.default.setString("did",t.data[0].id))})})},methods:{update:function(e){var t=weex.requireModule("businessLauncher");console.log(t),t.openURL("showuser",null,!1,!0)},tohome:function(){var e=weex.requireModule("businessLauncher");console.log(e),e.openURL("home",null,!1,!0)},totest:function(){var e=weex.requireModule("businessLauncher");console.log(e),e.openURL("test3",null,!1,!0)},callwxjs:function(){console.log("111111111112"),s.default.sendMessageToid("currentuser","getup")},register:function(){var e=this;s.default.getDeviceId(function(t){s.default.getDeviceName(function(n){u.fetch({method:"POST",url:"https://d.apicloud.com/mcm/api/device",type:"json",headers:a.default.getttt(),body:'{"did": "'+t+'","name":"'+n+'" }'},function(t){console.log("11111111"+JSON.stringify(t.data)),t.ok&&(console.log("111111"+t.data.id),e.noteMessage=!1,c.toast({message:"注册成功",duration:.3}),s.default.setString("did",t.data.id))})})})}}}}});