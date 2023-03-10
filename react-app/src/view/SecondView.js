import {Link} from "react-router-dom";


export default function SecondVIew() {

  return (
    <div>
      <p>
        This is from SecondView.js
      </p>
      <p className='tag-manager'>
        Hover to generate mouseover event
      </p>
      <button id='button-second-view' className='tag-manager'>
        Press to generate click event
      </button>
      <Link to='/'><p id='button-second-back' className='tag-manager'>back</p></Link>
    </div>
  )
}