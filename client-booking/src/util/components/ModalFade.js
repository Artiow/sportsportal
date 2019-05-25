import React from 'react';
import $ from 'jquery';

/**
 * ModalFade by bootstrap modal.
 * @author Artem Namednev <namednev.artem@gmail.com>
 */
export default class ModalFade extends React.Component {

    // noinspection JSUnusedGlobalSymbols
    activate(options) {
        $(this.modal).modal(options);
    }

    render() {
        const defClassName = 'ModalFade modal fade';
        const {className, ...props} = this.props;
        return (
            <div className={className ? `${className} ${defClassName}` : `${defClassName}`}
                 ref={modal => this.modal = modal}
                 tabIndex="-1"
                 {...props}>
                {this.props.children}
            </div>
        );
    }
}