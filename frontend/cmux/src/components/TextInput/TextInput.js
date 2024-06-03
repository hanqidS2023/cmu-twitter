// TextInput Component
import React from "react";
import "./TextInput.css";

function TextInput({type = "text", text, value, onChange }) {
  const [clicked, setClicked] = React.useState(false);
  const [inputFocus, setInputFocus] = React.useState(false);

  function isValueSet() {
    if (value === "") {
      setClicked(false);
    } else {
      setClicked(true);
    }
    setInputFocus(false);
  }

  return (
    <div
      className={inputFocus ? "textInputRow textInputRowActive" : "textInputRow"}
    >
      <label
        htmlFor={text}
        className={clicked ? "textInputLabel textInputLabelActive" : "textInputLabel"}
      >
        {text}
      </label>
      <input
        type={type}
        className="textInput"
        id={text}
        name={text}
        value={value}
        onChange={onChange}
        onFocus={() => {
          setInputFocus(true);
          setClicked(true);
        }}
        onBlur={() => isValueSet()}
      />
    </div>
  );
}

export default TextInput;
