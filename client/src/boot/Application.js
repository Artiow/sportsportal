import React from 'react';
import {Switch, withRouter} from 'react-router-dom';
import '../../node_modules/jquery/dist/jquery.min';
import '../../node_modules/popper.js/dist/umd/popper';
import '../../node_modules/bootstrap/dist/js/bootstrap.min';
import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../../node_modules/font-awesome/css/font-awesome.min.css';
import ScrollRoute from '../util/components/ScrollRoute';
import Confirmation from './auth/Confirmation';
import MainFrame from './frame/MainFrame';
import NoMatch from './mismatch/NoMatch';
import PlaygroundSearcher from '../main/index/PlaygroundSearcher';
import PlaygroundInfo from '../main/playground/PlaygroundInfo';
import OrderInfo from '../main/order/OrderInfo';
import verify, {login, logout} from '../util/verification';

const AppContext = React.createContext(null);

export function withAppContext(Component) {
    return function ContextualComponent(props) {
        return (
            <AppContext.Consumer>
                {app => <Component {...props} app={app}/>}
            </AppContext.Consumer>
        )
    }
}

export default withRouter(class Application extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            credentials: null,
            reVerify: this.reVerify.bind(this),
            reLogin: this.reLogin.bind(this),
            logout: this.logout.bind(this),
            login: this.login.bind(this)
        }
    }

    componentDidMount() {
        this.reVerify();
    }

    reVerify(event) {
        if (event != null) event.preventDefault();
        verify(
            data => this.setState({credentials: data}),
            error => this.setState({credential: null})
        );
    }

    reLogin(event) {
        if (event != null) event.preventDefault();
        localStorage.setItem('re_login', true);
        this.props.history.push('/');
    }

    logout(event) {
        if (event != null) event.preventDefault();
        logout();
        this.setState({
            credentials: null
        });
    }

    login(data) {
        login(data);
        this.reVerify()
    }

    render() {
        return (
            <AppContext.Provider
                value={this.state}>
                <Switch>
                    <ScrollRoute exact path='/' component={IndexFrame}/>
                    <ScrollRoute exact path='/home' component={HomeFrame}/>
                    <ScrollRoute exact path='/confirm' component={Confirmation}/>
                    <ScrollRoute exact path='/order/id:identifier' component={OrderFrame}/>
                    <ScrollRoute exact path='/playground/id:identifier' component={PlaygroundFrame}/>
                    <ScrollRoute component={NoMatch}/>
                </Switch>
            </AppContext.Provider>
        );
    }
})

function IndexFrame(props) {
    const frameProps = {header: {breadcrumb: []}};
    return (
        <MainFrame {...frameProps}>
            <PlaygroundSearcher/>
        </MainFrame>
    );
}

function PlaygroundFrame(props) {
    const frameProps = {
        header: {breadcrumb: buildBreadcrumb(props.location.pathname, 'Просмотр площадки')}
    };
    return (
        <MainFrame {...frameProps}>
            <PlaygroundInfo identifier={props.match.params.identifier}/>
        </MainFrame>
    );
}

function OrderFrame(props) {
    const frameProps = {
        header: {breadcrumb: buildBreadcrumb(props.location.pathname, 'Бронирование')}
    };
    return (
        <MainFrame {...frameProps}>
            <OrderInfo identifier={props.match.params.identifier}/>
        </MainFrame>
    );
}

function HomeFrame(props) {
    const frameProps = {
        header: {breadcrumb: buildBreadcrumb(props.location.pathname, 'Домашняя страница')}
    };
    return (
        <MainFrame {...frameProps}>
            <main className="Home container"/>
        </MainFrame>
    );
}

function buildBreadcrumb(link, title) {
    return [{link: link, title: title,}];
}