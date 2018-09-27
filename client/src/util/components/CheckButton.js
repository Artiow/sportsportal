import React, {Component} from "react";

/**
 * CheckButton by bootstrap button.
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return carousel component
 */
class CheckButton extends Component {
    constructor(props) {
        super(props);
        this.activeClassName = 'CheckButton btn btn-sm btn-dark';
        this.inactiveClassName = 'CheckButton btn btn-sm btn-outline-dark';
        this.state = {active: (((props.checked === 'checked') || (props.checked === true)))};
    }

    switch(event) {
        this.setState({active: event.target.checked});
        const onChange = this.props.onChange;
        if (typeof onChange === 'function') onChange(event);
    }

    render() {
        // hidden="hidden"
        return (
            <label className={this.state.active ? this.activeClassName : this.inactiveClassName}>
                {this.props.content}<input type="checkbox" checked={this.state.active} hidden={true}
                                           onChange={this.switch.bind(this)}/>
            </label>
        )
    }
}

export default CheckButton;