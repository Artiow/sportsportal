import React from 'react';

/**
 * CustomCheckbox by bootstrap custom-control.
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return carousel component
 */
export default function CustomCheckbox(props) {
    return (
        <div className="CustomCheckbox custom-control custom-checkbox">
            <input type="checkbox" className="custom-control-input"
                   id={props.id} value={props.value} onChange={props.onChange}/>
            <label className="custom-control-label" htmlFor={props.id}>{props.children}</label>
        </div>
    )
}