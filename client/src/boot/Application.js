import React, {Component} from 'react';
import {Route, Switch} from 'react-router-dom';
import '../../node_modules/jquery/dist/jquery.min';
import '../../node_modules/popper.js/dist/umd/popper';
import '../../node_modules/bootstrap/dist/js/bootstrap.min';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../../node_modules/font-awesome/css/font-awesome.min.css';
import Home from './home/Home';
import Login from './auth/Login';
import Registration from './auth/Registration';
import Header from '../header/Header';
import Main from '../main/Main';

class Application extends Component {
    render() {
        return (
            <Switch>
                <Route exact path='/registration' component={Registration}/>
                <Route exact path='/login' component={Login}/>
                <Route exact path='/home' component={Home}/>
                <Route path='/' component={MainFrame}/>
            </Switch>
        );
    }
}

class MainFrame extends Component {
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
