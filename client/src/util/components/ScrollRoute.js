import React from 'react';
import {Route, withRouter} from 'react-router-dom';

export default withRouter(class ScrollRoute extends React.Component {

    componentDidUpdate(prevProps) {
        if (this.props.location !== prevProps.location) {
            window.scrollTo(0, 0)
        }
    }

    render() {
        const {component: Component, ...rest} = this.props;
        return <Route {...rest} render={props => (<Component {...props} />)}/>;
    }
});