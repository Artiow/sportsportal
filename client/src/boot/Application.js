import React from 'react';
import {Switch} from 'react-router-dom';
import '../../node_modules/jquery/dist/jquery.min';
import '../../node_modules/popper.js/dist/umd/popper';
import '../../node_modules/bootstrap/dist/js/bootstrap.min';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../../node_modules/font-awesome/css/font-awesome.min.css';
import ScrollRoute from '../util/components/ScrollRoute';
import Login from './auth/Login';
import Registration from './auth/Registration';
import NoMatch from './mismatch/NoMatch';
import Header from './enviroment/Header';
import Footer from './enviroment/Footer';
import Index from '../main/Index';
import Playground from '../main/Playground';

function Application(props) {
    return (
        <Switch>
            <ScrollRoute exact path='/' component={IndexFrame}/>
            <ScrollRoute exact path='/playground/id:identifier' component={PgFrame}/>
            <ScrollRoute exact path='/registration' component={Registration}/>
            <ScrollRoute exact path='/login' component={Login}/>
            <ScrollRoute exact path='/home' component={HomeFrame}/>
            <ScrollRoute component={NoMatch}/>
        </Switch>
    );
}

function HomeFrame(props) {
    return (
        <div>
            <Header titleHref={'/home'} titleLabel={'ДОМАШНЯЯ СТРАНИЦА'}
                    subtitleHref={'/'} subtitleLabel={'НА ГЛАВНУЮ'}/>
            <div className="container" style={{minHeight: "95vh"}}/>
            <Footer/>
        </div>
    );
}

function IndexFrame(props) {
    return (
        <div>
            <Header titleHref={'/'} titleLabel={'АРЕНДА ПЛОЩАДОК'}/>
            <Index/>
            <Footer/>
        </div>
    );
}

function PgFrame(props) {
    return (
        <div>
            <Header titleHref={'/'} titleLabel={'АРЕНДА ПЛОЩАДОК'}
                    subtitleHref={'/'} subtitleLabel={'НА ГЛАВНУЮ'}/>
            <Playground identifier={props.match.params.identifier}/>
            <Footer/>
        </div>
    );
}

export default Application;
