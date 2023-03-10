import {Link, Route, Routes, useLocation} from "react-router-dom";


export default function MainView() {

  return (
    <div>
      <Link to='/first'><li id='button-to-first' className={'tag-manager'}>link to first</li></Link>
      <Link to='/second'><li id='button-to-second' className={'tag-manager'}>link to second</li></Link>
    </div>
  );
}