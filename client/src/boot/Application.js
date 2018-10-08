import React from 'react';
import {Switch} from 'react-router-dom';
import '../../node_modules/jquery/dist/jquery.min';
import '../../node_modules/popper.js/dist/umd/popper';
import '../../node_modules/bootstrap/dist/js/bootstrap.min';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../../node_modules/font-awesome/css/font-awesome.min.css';
import ScrollRoute from '../util/components/ScrollRoute';
import Registration from './auth/Registration';
import Confirmation from './auth/Confirmation';
import MainFrame from './frame/MainFrame';
import NoMatch from './mismatch/NoMatch';
import Index from '../main/Index';
import Playground from '../main/Playground';
import Order from '../main/Order';

function Application(props) {
    return (
        <Switch>
            <ScrollRoute exact path='/' component={IndexFrame}/>
            <ScrollRoute exact path='/playground/id:identifier' component={PlaygroundFrame}/>
            <ScrollRoute exact path='/order/id:identifier' component={OrderFrame}/>
            <ScrollRoute exact path='/registration' component={Registration}/>
            <ScrollRoute exact path='/confirm' component={Confirmation}/>
            <ScrollRoute exact path='/home' component={HomeFrame}/>
            <ScrollRoute component={NoMatch}/>
        </Switch>
    );
}

function IndexFrame(props) {
    const frameProps = {
        header: {
            titleHref: '/',
            titleLabel: 'АРЕНДА ПЛОЩАДОК'
        }
    };
    return (
        <MainFrame {...frameProps}>
            <Index/>
        </MainFrame>
    );
}

function PlaygroundFrame(props) {
    const frameProps = {
        header: {
            titleHref: '/',
            titleLabel: 'АРЕНДА ПЛОЩАДОК',
            subtitleHref: '/',
            subtitleLabel: 'НА ГЛАВНУЮ'
        }
    };
    return (
        <MainFrame {...frameProps}>
            <Playground identifier={props.match.params.identifier}/>
        </MainFrame>
    );
}

function OrderFrame(props) {
    const orderId = props.match.params.identifier;
    const frameProps = {
        header: {
            titleHref: '/order/id' + orderId,
            titleLabel: 'ОФОРМЛЕНИЕ БРОНИРОВАНИЯ',
            subtitleHref: '/',
            subtitleLabel: 'НА ГЛАВНУЮ'
        }
    };
    return (
        <MainFrame {...frameProps}>
            <Order identifier={orderId}/>
        </MainFrame>
    );
}

function HomeFrame(props) {
    const frameProps = {
        header: {
            titleHref: '/home',
            titleLabel: 'ДОМАШНЯЯ СТРАНИЦА',
            subtitleHref: '/',
            subtitleLabel: 'НА ГЛАВНУЮ'
        }
    };
    return (
        <MainFrame {...frameProps}>
            <main className="Home container"/>
        </MainFrame>
    );
}

export default Application;
