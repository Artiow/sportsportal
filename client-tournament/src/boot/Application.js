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
import IndexPage from '../main/index/IndexPage';
import HomePage from '../main/home/HomePage';

const ApplicationContext = React.createContext(null);

export function withApplicationContext(Component) {
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
            login: this.login.bind(this),
            logout: this.logout.bind(this)
        }
    }

    componentDidMount() {
        this.login();
    }

    preLogin(event) {
        if (event) event.preventDefault();
        localStorage.setItem('pre_login', true);
        this.props.history.push('/');
    }

    login(event) {
        if (event) event.preventDefault();
        Authentication.access()
            .then(token => {
                console.debug('Application', 'login', 'access granted');
                User.get(JSON.parse(window.atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/'))).userId)
                    .then(data => {
                        console.debug('Application', 'login', 'login successful');
                        this.setState({principal: data});
                    })
                    .catch(error => {
                        console.error('Application', 'login', 'user data getting error');
                        this.setState({principal: null});
                    });
            })
            .catch(error => {
                console.warn('Application', 'login', 'access denied');
                this.setState({principal: null});
            });
    }

    logout(event) {
        if (event) event.preventDefault();
        console.debug('Application', 'logout', 'logout successful');
        this.setState({principal: null});
    }

    render() {
        return (
            <ApplicationContext.Provider
                value={this.state}>
                <Switch>
                    <ScrollRoute exact path='/' component={IndexFrame}/>
                    <ScrollRoute exact path='/home' component={HomeFrame}/>
                    <ScrollRoute exact path='/confirm' component={Confirmation}/>
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
            <IndexPage/>
        </MainFrame>
    );
}

function HomeFrame(props) {
    const frameProps = {
        header: {breadcrumb: buildBreadcrumb(props.location.pathname, 'Домашняя страница')}
    };
    return (
        <MainFrame {...frameProps}>
            <HomePage/>
        </MainFrame>
    );
}

function buildBreadcrumb(link, title) {
    return [{link: link, title: title,}];
}