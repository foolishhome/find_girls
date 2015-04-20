/**
 * File : js/common.js
 * Author : Donson
 * version : v0.1
 * LastEdit : 07/05/13 01:29:25
 * =============
 */


/**
 * _common
 */
var _common = window._common || {};

_common = {
    init : function(){
        //关闭提示
        $('.g_tips').find('.close').click(function(){
            $(this).parents('.g_tips').remove();
        });

        //$('body').timeago();

    },
    popCreate : function(options,callback){
        var _ts = this,
            fun = function(){},
            defaultsOptions = {
                type    : 'window',
                id      : '',
                title   : '标题',
                url     : '',       //iframe地址
                afterShow   : fun
            },
            opt = $.extend({}, defaultsOptions, options || {});

        if(opt.id === ''){
            //自动设置id
            opt.id = 'pop_' + (new Date()).valueOf();
        }

        //窗口
        if(opt.type === 'window'){
            _ts.popCreateWindow(opt);
        }else if(opt.type === 'slide'){
            _ts.popSlide(opt);
        }else if(opt.type === 'dialog') {
            _ts.popDialog(opt,callback)
        }
    },
    popCreateWindow : function(opt){

        var _ts = this,
            html = '<div id="'+opt.id+'" class="pop_window1"><div class="pw_hd"><h3>'+opt.title+'</h3><span class="pw_close" title="关闭"></span></div><div class="pw_bd" style="height: 460px;"><div class="pw_loading"></div><div class="pw_con hidden" style="height: 460px"><iframe width="616" height="460" src="'+opt.url+'" frameborder="0" scrolling="no"></iframe></div></div></div>';

        _ts.overlay();

        //append html to pop_dialogs
        var _popw = $('#pop_dialogs');
        if(!_popw.get(0)){
            $('body').append('<div id="pop_dialogs"></div>');
        }
        $('#pop_dialogs').append(html);

        _ts.cur = opt.id;

        var _el = $('#'+opt.id);

        _el.find('.pw_close').click(function(){
            _ts.closePop();
        });

        setTimeout(function(){
            _el.find('.pw_loading').hide();
            _el.find('.pw_con').show();
        }, 300);
    },
    //弹层
    pop : function(options,callback){
        var _ts = this,
            fun = function(){},
            defaultsOptions = {
                type    : 'window',
                id      : '',
                title   : '标题',
                url     : '',       //iframe地址
                afterShow   : fun
            },
            opt = $.extend({}, defaultsOptions, options || {});

        if(opt.id === ''){
            //自动设置id
            opt.id = 'pop_' + (new Date()).valueOf();
        }

        //窗口
        if(opt.type === 'window'){
            _ts.popWindow(opt);
        }else if(opt.type === 'slide'){
            _ts.popSlide(opt);
        }else if(opt.type === 'dialog') {
            _ts.popDialog(opt,callback)
        }
    },

    popDialog:function(opt,callback){
        var confirm_btn = '<a class="g_btn confirm_btn" href="javascript:void(0)"><span>确　定</span></a>';
        var cancel_btn = '<a class="g_btn cancel_btn" href="javascript:void(0)"><span>取　消</span></a>';
        if(!opt.confirm){
            confirm_btn = ''
        }
        if(!opt.cancel){
           cancel_btn = '';
        }

        var _ts = this,
            html = '<div id="'+opt.id+'" class="pop_dialog"><div class="pw_hd"><h3>'+opt.title+'</h3><span class="pw_close" title="关闭"></span></div><div class="pw_bd"><div class="pw_loading"></div><div class="pw_con hidden" >' +
                '<p style="width: 260px">'+opt.content+'</p>' +
                '<div class="btns">' + cancel_btn + confirm_btn +  '</div>'+
                '</div></div></div>';

        _ts.overlay();

        //append html to pop_dialogs
        var _popw = $('#pop_dialogs');
        if(!_popw.get(0)){
            $('body').append('<div id="pop_dialogs"></div>');
        }
        $('#pop_dialogs').append(html);

        _ts.cur = opt.id;

        var _el = $('#'+opt.id);

        _el.find('.pw_close').click(function(){
            _ts.closePop();
        });
        _el.find('.cancel_btn').click(function(){
            if(callback){
                callback(false);
            }
            _ts.closePop();
        })
        _el.find('.confirm_btn').click(function(){
            if(callback){
                callback(true);
            }
            _ts.closePop();
        })

        _el.find('.pw_loading').hide();
        _el.find('.pw_con').show();
    },
    popWindow : function(opt){

        var _ts = this,
            html = '<div id="'+opt.id+'" class="pop_window"><div class="pw_hd"><h3>'+opt.title+'</h3><span class="pw_close" title="关闭"></span></div><div class="pw_bd"><div class="pw_loading"></div><div class="pw_con hidden"><iframe width="616" height="410" src="'+opt.url+'" frameborder="0" scrolling="no"></iframe></div></div></div>';

        _ts.overlay();

        //append html to pop_dialogs
        var _popw = $('#pop_dialogs');
        if(!_popw.get(0)){
            $('body').append('<div id="pop_dialogs"></div>');
        }
        $('#pop_dialogs').append(html);

        _ts.cur = opt.id;

        var _el = $('#'+opt.id);

        _el.find('.pw_close').click(function(){
            _ts.closePop();
        });

        setTimeout(function(){
            _el.find('.pw_loading').hide();
            _el.find('.pw_con').show();
        }, 300);
    },
    popSlide : function(opt){
        var _ts = this,
            _window = $(window),
            style = 'width:'+_window.width()+'px;height:'+_window.height()+'px;',
            html = '<div id="'+opt.id+'" class="pop_slide" style="'+style+'"><div style="'+style+'" class="ps_bd"><div style="'+style+'" class="ps_loading"></div><div class="ps_con hidden" style="'+style+'"><iframe width="'+_window.width()+'" height="'+_window.height()+'" src="'+opt.url+'&width='+_window.width()+'&height='+_window.height()+'" frameborder="0" scrolling="no" onload="this.focus();"></iframe></div></div></div>';

        _ts.overlay();

        //append html to pop_dialogs
        var _popw = $('#pop_dialogs');
        if(!_popw.get(0)){
            $('body').append('<div id="pop_dialogs"></div>');
        }
        $('#pop_dialogs').append(html);

        _ts.cur = opt.id;

        var _el = $('#'+opt.id);

        /*_el.find('.pw_close').click(function(){
         _ts.closePop();
         });*/

        setTimeout(function(){
            _el.find('.ps_loading').hide();
            _el.find('.ps_con').show();
        }, 300);
    },
    closePop : function(){
        var _ts = this;
        try{
            $('#'+_ts.cur).remove();
        }catch(e){
        };
        _ts.removeOverlay();
        _ts.cur = '';
    },
    overlay : function(){
        var overlay = $('#pop_overlay'),
            _window = $(window),
            over_style = 'width:'+_window.width()+'px;height:'+_window.height()+'px;';
        if(!overlay.get(0)){
            $('body').append('<div id="pop_overlay" style="'+over_style+'"></div>');
        }
    },
    removeOverlay : function(){
        $('#pop_overlay').remove();
    },
    removeAllPop: function(){
        $('#pop_overlay').remove();
        $('.pop_slide').remove();
    },
    //通用提示
    tips : function(txt, type){
        if($('.g_tips2').size()>0){
            $('.g_tips2').remove();
        }

        var ico = '';
        if(type === 'right' || type === 'wrong' || type === 'warn' || type === 'notice'){
            ico = '<em class="ico_'+type+'"></em>';
        }

        $('body').append('<div id="g_tips2" class="g_tips2"><p class="'+type+'">'+ico+txt+'</p></div>');
        setTimeout(function(){
            $('#g_tips2').fadeOut(function(){
                $(this).remove();
            });
        }, 3000);
    },
    getQuery : function(name){
        var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
        if (reg.test(location.href)){
            return window.unescape(RegExp.$2.replace(/\+/g, " "));

        }
        return "";
    },
    normalWindow: function(){
        try{
            window.parent.window.MainWindow._showNormal();
        }catch (e){}
    },
    fullWindow: function(){
        try{
            MainWindow._showFullScreen();
        }catch (e){}
    },
    hasPlugin : function(name) {
        name = name.toLowerCase();
        for (var i = 0; i < navigator.plugins.length; i++) {
            if (navigator.plugins[i].name.toLowerCase().indexOf(name) > -1) {
                return true;
            }
        }
        return false;
    },
    setCookie:  function (c_name,value,expiredays){
        var exdate=new Date()
        exdate.setDate(exdate.getDate()+expiredays)
        document.cookie=c_name+ "=" +escape(value)+
            ((expiredays==null) ? "" : ";expires="+exdate.toGMTString()) + ";path=/"
    },
    setCookie_exp_minutes:  function (c_name,value,expire){
        var exdate=new Date()
        exdate.setMinutes(exdate.getMinutes()+expire)
        document.cookie=c_name+ "=" +escape(value)+
            ((expire==null) ? "" : ";expires="+exdate.toGMTString()) + ";path=/"
    },
    getCookie: function (c_name) {
        if (document.cookie.length>0)
        {
            c_start=document.cookie.indexOf(c_name + "=")
            if (c_start!=-1)
            {
                c_start=c_start + c_name.length+1
                c_end=document.cookie.indexOf(";",c_start)
                if (c_end==-1) c_end=document.cookie.length
                return unescape(document.cookie.substring(c_start,c_end))
            }
        }
        return ""
    } ,
    delCookie: function (name){
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval=_common.getCookie(name);
        if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString() +  ";path=/"
    },
    handleTips: function(){
        var addExp = _common.getCookie("_add_exp");
        //alert(addExp)
        if(addExp != ''){
            var v = JSON.parse(addExp)
            if(v.createshow != undefined && v.createshow > 0){
              _common.tips("发布爆照,恭喜您获得了"+ v.createshow+"经验",'right');
            }
            else if(v.updateshow != undefined && v.updateshow > 0){
              _common.tips("更新爆照,恭喜您获得了"+ v.updateshow+"经验",'right');
            }
            else if(v.createwooer != undefined && v.createwooer > 0){
              _common.tips("应征爆照,恭喜您获得了"+ v.createwooer+"经验",'right');
            }
            else if(v.updatewooer != undefined && v.updatewooer > 0){
              _common.tips("更新应征,恭喜您获得了"+ v.updatewooer+"经验",'right');
            }
            else if(v.login != undefined && v.login > 0){
              _common.tips("登录获得"+ v.login+"经验",'right');
            }
            else{
               if(v.login == undefined){
                 _common.tips("您今天的经验值已领取完毕")
               }
            }
        }
        _common.delCookie('_add_exp');
    },
    clip: function(text,callback){
        try{
            MainWindow._setTextToClipboard(text);
            if (callback){
                callback();
            }
        }catch(e){
        };
    },
    closeWindow: function(){
        try{
            MainWindow._closeMainWindow();
        }catch (e){};
    }
};




//入口
$(function(){
    //init
    _common.init();
    _common.handleTips();
});