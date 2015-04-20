/**
 * File : js/pop.js
 * Author : Donson
 * version : v0.1
 * LastEdit : 05/05/13 10:03:29
 * =============
 */


/**
 * _pop
 */
var _iframe = window._iframe || {};

//common
_iframe.common = {
    init : function(){
        var _ts = this;
        $('.create_show').click(function(){
            _common.popCreate({
                title : '我要爆照',
                url : '/shows/new',
                create: 'create show'
            });
            return false;
        });
        $('.cannot_create_show').click(function(){
            _common.tips(' 你今天非常努力，休息一下明天继续!','notice');
            return false;
        });
        $('.update_show').click(function(){
            var id = $(this).attr("data")
            _common.pop({
                title : '更新爆照',
                url : '/shows/'+id+"/edit"
            });
            return false;
        });
        $('.create_wooer').click(function(){
            var id = $(this).attr("data")
            _common.pop({
                title : '我要应征',
                url : '/wooers/new?show_id='+id
            });
            return false;
        });
        $('.cannot_create_wooer').click(function(){
            _common.tips(' 你今天非常努力，休息一下明天继续!','notice');
            return false;
        });
        $('.update_wooer').click(function(){
            var id = $(this).attr("data")
            _common.pop({
                title : '更新应征',
                url : '/wooers/'+id+"/edit"
            });
            return false;
        });
        if(_common.hasPlugin('flash')){
            $('.update_figure').click(function(){
                    _common.pop({
                        title : '更换头像',
                        url : '/users/figure'
                    });
                return false;
            });
        }else{
           _upload.common.upload_figure();
        }

    }
};




//入口
$(function(){

    //common
    _iframe.common.init();

});
