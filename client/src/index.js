import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter} from 'react-router-dom';

import './index.css';
import Application from './boot/Application';
import register from './util/registerServiceWorker';

ReactDOM.render((
    <BrowserRouter>
        <Application/>
    </BrowserRouter>
), document.getElementById('root'));
register();
