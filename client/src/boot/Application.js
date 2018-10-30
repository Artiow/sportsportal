import React from 'react';
import {Switch, withRouter} from 'react-router-dom';
import Authentication from '../connector/Authentication';
import User from '../connector/User';
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

const ApplicationContext = React.createContext(null);

export function withAppContext(Component) {
    return function ContextualComponent(props) {
        return (
            <ApplicationContext.Consumer>
                {application => <Component {...props} application={application}/>}
            </ApplicationContext.Consumer>
        )
    }
}

export default withRouter(class Application extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            principal: null,
            preLogin: this.preLogin.bind(this),
            logout: this.logout.bind(this),
            login: this.login.bind(this)
        }
    }

    componentDidMount() {
        this.login();
    }

    preLogin(event) {
        if (event != null) event.preventDefault();
        localStorage.setItem('pre_login', true);
        this.props.history.push('/');
    }

    logout(event) {
        if (event != null) event.preventDefault();
        Authentication.logout()
            .then(() => {
                console.debug('Application [logout]: logout successful');
                this.setState({principal: null});
            })
            .catch(error => {
                console.error('Application [logout]:', error ? error : 'logout error');
                this.setState({principal: null});
            });
    }

    login(event) {
        if (event != null) event.preventDefault();
        Authentication.access()
            .then(token => {
                console.debug('Application [login]: access granted');
                User.get(JSON.parse(window.atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/'))).userId)
                    .then(data => {
                        console.debug('Application [login]: login successful');
                        this.setState({principal: data});
                    })
                    .catch(error => {
                        console.error('Application [login]:', error ? error : 'user data getting error');
                        this.setState({principal: null});
                    });
            })
            .catch(error => {
                console.error('Application [login]:', error ? error : 'access denied');
                this.setState({principal: null});
            })
    }

    render() {
        return (
            <ApplicationContext.Provider
                value={this.state}>
                <Switch>
                    <ScrollRoute exact path='/' component={IndexFrame}/>
                    <ScrollRoute exact path='/home' component={HomeFrame}/>
                    <ScrollRoute exact path='/confirm' component={Confirmation}/>
                    <ScrollRoute exact path='/order/id:identifier' component={OrderFrame}/>
                    <ScrollRoute exact path='/playground/id:identifier' component={PlaygroundFrame}/>
                    <ScrollRoute component={NoMatch}/>
                </Switch>
            </ApplicationContext.Provider>
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