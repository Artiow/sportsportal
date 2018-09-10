import React, {Component} from 'react';
import '../../node_modules/jquery/dist/jquery.min';
import '../../node_modules/popper.js/dist/umd/popper';
import '../../node_modules/bootstrap/dist/js/bootstrap.min';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../../node_modules/font-awesome/css/font-awesome.min.css';
import Header from '../header/Header';
import Main from '../main/Main';

class Application extends Component {
    render() {
        return (
            <div>
                <Header/>
                <Main/>
            </div>
        );
    }
}

export default Application;
