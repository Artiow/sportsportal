import React from 'react';

export default function PlaygroundSubmitOrderModal(props) {

    const CLOSE_TITLE = 'Отмена';
    const SUBMIT_TITLE = 'Забронировать';

    // template
    const totalPrice = '0000';

    return (
        <div id={props.identifier} className="PlaygroundOrderModal modal fade" tabIndex="-1">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">
                            Подтвердите правильность выбора
                        </h5>
                        <button type="button" className="close" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <div className="modal-body">
                        ...
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-dismiss="modal">
                            {CLOSE_TITLE}
                        </button>
                        <button type="submit" className="btn btn-success">
                            {SUBMIT_TITLE}
                            <span className="badge badge-dark ml-1">
                                {totalPrice}<i className="fa fa-rub ml-1"/>
                            </span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}