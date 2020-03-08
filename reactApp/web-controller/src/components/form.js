import React from "react";

const Form = props => (
    <form onSubmit={props.wetherMethod}>
        <input type="text" name="city" placeholder="City" />
        <button>Get Wether</button>
    </form>
);

export default Form;