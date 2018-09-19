import React from 'react';
import {Route, Switch} from 'react-router-dom';
import '../../node_modules/jquery/dist/jquery.min';
import '../../node_modules/popper.js/dist/umd/popper';
import '../../node_modules/bootstrap/dist/js/bootstrap.min';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../../node_modules/font-awesome/css/font-awesome.min.css';
import Login from './auth/Login';
import Registration from './auth/Registration';
import NoMatch from './mismatch/NoMatch';
import Header from './header/Header';
import Index from '../main/Index';
import Playground from '../main/Playground';

function Application(props) {
    return (
        <Switch>
            <Route exact path='/' component={IndexFrame}/>
            <Route exact path='/playground/id:identifier' component={PgFrame}/>
            <Route exact path='/registration' component={Registration}/>
            <Route exact path='/login' component={Login}/>
            <Route exact path='/home' component={HomeFrame}/>
            <Route component={NoMatch}/>
        </Switch>
    );
}

function HomeFrame(props) {
    return (
        <div>
            <Header titleHref={'/home'} titleLabel={'ДОМАШНЯЯ СТРАНИЦА'}
                    subtitleHref={'/'} subtitleLabel={'НА ГЛАВНУЮ'}/>
            <div className="container"/>
        </div>
    );
}

function IndexFrame(props) {
    return (
        <div>
            <Header titleHref={'/'} titleLabel={'АРЕНДА ПЛОЩАДОК'}/>
            <Index/>
        </div>
    );
}

function PgFrame(props) {
    return (
        <div>
            <Header titleHref={'/'} titleLabel={'АРЕНДА ПЛОЩАДОК'}
                    subtitleHref={'/'} subtitleLabel={'НА ГЛАВНУЮ'}/>
            <Playground identifier={props.match.params.identifier}/>
        </div>
    );
}

export default Application;
