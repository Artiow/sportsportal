import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter} from 'react-router-dom';

import './index.css';
import Application from './boot/Application';
import register from './util/registerServiceWorker';

// define a new console
const console = (function (nativeConsole) {
    return {
        debug: function (message, ...optionalParams) {
            nativeConsole.log(message, ...optionalParams);
        },
        log: function (message, ...optionalParams) {
            nativeConsole.log(message, ...optionalParams);
        },
        info: function (message, ...optionalParams) {
            nativeConsole.info(message, ...optionalParams);
        },
        warn: function (message, ...optionalParams) {
            nativeConsole.warn(message, ...optionalParams);
        },
        error: function (message, ...optionalParams) {
            nativeConsole.error(message, ...optionalParams);
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
