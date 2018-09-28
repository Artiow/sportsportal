import React, {Component} from "react";

/**
 * CheckButton by bootstrap button.
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return carousel component
 */
class CheckButton extends Component {

    static ACTIVE_CLASS_NAME = 'CheckButton btn btn-sm btn-dark';
    static INACTIVE_CLASS_NAME = 'CheckButton btn btn-sm btn-outline-dark';

    constructor(props) {
        super(props);
        this.state = {active: ((props.checked === 'checked') || (props.checked === true))};
    }

    componentWillReceiveProps(nextProps) {
        if ((this.props.value !== nextProps.value) && (this.props.checked !== nextProps.checked)) {
            this.setState({active: ((nextProps.checked === 'checked') || (nextProps.checked === true))});
        }
    }

    switch(event) {
        this.setState({active: event.target.checked});
        const onChange = this.props.onChange;
        if (typeof onChange === 'function') onChange(event);
    }

    render() {
        return (
            <label id={this.props.id} className={
                this.state.active
                    ? CheckButton.ACTIVE_CLASS_NAME
                    : CheckButton.INACTIVE_CLASS_NAME
            }>
                {this.props.content}
                <input type="checkbox" value={this.props.value} hidden={true}
                       checked={this.state.active} onChange={this.switch.bind(this)}/>
            </label>
        )
    }
}

export default CheckButton;