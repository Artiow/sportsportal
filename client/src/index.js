import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter} from 'react-router-dom';

import './index.css';
import Application from './boot/Application';
import register from './util/registerServiceWorker';

// define a new console
const console = (function (nativeConsole) {
    const prefix = function (sender, action) {
        action = `[${action}]:`;
        const FILLER = '                                  ';
        return sender + FILLER.substring(sender.length, FILLER.length - action.length) + action;
    };
    return {
        debug: function (sender, action, message, ...optionalParams) {
            nativeConsole.debug(' ', prefix(sender, action), message, ...optionalParams);
        },
        log: function (sender, action, message, ...optionalParams) {
            nativeConsole.log(' ', prefix(sender, action), message, ...optionalParams);
        },
        info: function (sender, action, message, ...optionalParams) {
            nativeConsole.info(' ', prefix(sender, action), message, ...optionalParams);
        },
        warn: function (sender, action, message, ...optionalParams) {
            nativeConsole.warn(prefix(sender, action), message, ...optionalParams);
        },
        error: function (sender, action, message, ...optionalParams) {
            nativeConsole.error(prefix(sender, action), message, ...optionalParams);
        }
    };
}(window.console));

// noinspection JSValidateTypes
window.console = console;

ReactDOM.render((
    <BrowserRouter>
        <Application/>
    </BrowserRouter>
), document.getElementById('root'));
register();
