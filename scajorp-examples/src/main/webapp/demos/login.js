/**
 * Scajorp - Login Page Example
 *
 * http://code.google.com/p/scajorp/
 */

Ext.onReady(function(){

    var login = new Ext.FormPanel({
        labelWidth: 75,
        url:'TO_DO.XHTML',
        frame:true,
        title: 'Login form',
        bodyStyle:'padding:5px 5px 0',
        width: 350,
        defaults: {width: 230},
        defaultType: 'textfield',
           items: [{
                fieldLabel: 'Username',
                name: 'username',
                allowBlank:false
            },{
                fieldLabel: 'Password',
                name: 'password',
                inputType: 'password',
                allowBlank: false
            }
        ],

        buttons: [{
            text: 'Save'
        },{
            text: 'Cancel'
        }]
    });

    login.render(document.body);

});







