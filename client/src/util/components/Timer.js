import React from 'react';

/**
 * Timer.
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return timer component
 */
export default class Timer extends React.Component {
    constructor(props) {
        super(props);
        const minute = this.props.m;
        const second = this.props.s;
        this.state = {
            minute: minute ? minute : 0,
            second: second ? second : 0
        };
    }

    start() {
        this.interval = setInterval(this.tick.bind(this), 1000);
    }

    componentDidMount() {
        this.start();
    }

    componentWillReceiveProps(nextProps) {
        if (this.interval) clearInterval(this.interval);
        const minute = nextProps.m;
        const second = nextProps.s;
        this.setState({
            minute: minute ? minute : 0,
            second: second ? second : 0
        });
        this.start();
    }

    tick() {
        this.setState(prevState => {
            let minute = prevState.minute;
            let second = prevState.second;
            if ((minute > 0) || (second > 0)) {
                second--;
                if (second < 0) {
                    second = 59;
                    minute--;
                }
            }
            return {
                minute: minute,
                second: second
            };
        })
    }

    render() {
        let resultClass = 'Timer';
        const {divider, className, ...otherProps} = this.props;
        if (className) resultClass = resultClass + ` ${className}`;
        const numberOf = num => ((num < 10) ? ('0' + num) : (num));
        return (
            <span className={resultClass} {...otherProps}>
                {numberOf(this.state.minute) + (divider ? divider : ':') + numberOf(this.state.second)}
            </span>
        )
    }
}