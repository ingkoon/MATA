import {Link} from "react-router-dom";


export default function FirstVIew() {

  return (
    <div>
      <p>
        This is from FirstView.js
      </p>
      <p className='tag-manager'>
        Hover to generate mouseover event
      </p>
      <button id='button-first-view'>
        This is not tracked by tag-manager
      </button>
      <Link to='/'><p id='button-first-back' className='tag-manager'>back</p></Link>
    </div>
  )
}