import React from "react";

export default class ResetRedirect extends React.Component {

    componentDidMount() {
        localStorage.setItem('_modal', MainFrame.MODAL.RESET);
        this.props.history.push('/');
    }
}