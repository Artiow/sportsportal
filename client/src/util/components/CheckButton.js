import React from 'react';

/**
 * CheckButton by bootstrap button.
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return carousel component
 */
export default class CheckButton extends React.Component {

    static className = (props, checked) => {
        return `CheckButton btn ${props.sizeStyle} ${checked ? props.checkedStyle : props.uncheckedStyle}`;
    };

    constructor(props) {
        super(props);
        this.state = {checked: ((props.checked === 'checked') || (props.checked === true))};
    }

    componentWillReceiveProps(nextProps) {
        if (this.props.checked !== nextProps.checked) {
            this.setState({checked: ((nextProps.checked === 'checked') || (nextProps.checked === true))});
        }
    }

    switch(event) {
        this.setState({checked: event.target.checked});
        const onChange = this.props.onChange;
        if (typeof onChange === 'function') onChange(event);
    }

    render() {
        const checked = this.state.checked;
        return (
            <label id={this.props.id} className={CheckButton.className(this.props, checked)}>
                {this.props.content}
                <input type="checkbox" value={this.props.value} hidden={true}
                       checked={checked} onChange={this.switch.bind(this)}/>
            </label>
        )
    }
}