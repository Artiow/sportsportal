import React from 'react';
import $ from 'jquery';

/**
 * ModalFade by bootstrap modal.
 * @author Artem Namednev <namednev.artem@gmail.com>
 */
export default class ModalFade extends React.Component {
    show(options) {
        $(this.modal).modal(options);
    }

    render() {
        return (
            <div className="modal fade" tabIndex="-1"
                 ref={modal => this.modal = modal}>
                {this.props.children}
            </div>
        );
    }
}