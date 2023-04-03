import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
import {Route, Routes, useLocation} from "react-router-dom";
import FirstVIew from "./view/FirstVIew";
import SecondVIew from "./view/SecondView";
import MainView from "./view/MainView";
import TagManager from "./module";

const tagManager = new TagManager(
  "MATA 서비스 URL",
  "발급받은 토큰",
  ['클릭 이벤트', '스크롤 이벤트']
);

export default function App() {
  const location = useLocation()
  // if(document.getElementById("referrer-meta") == null) {
  //   // referrer 권한 추가
  //   var meta = document.createElement('meta');
  //   meta.id = "referrer-meta";
  //   meta.name = 'referrer';
  //   meta.content = "unsafe-url";
  //   document.getElementsByTagName('head')[0].appendChild(meta);
  // }
  useEffect(() => {
    tagManager.attach();
    return () => {
      tagManager.detach();
    }
  }, [location])

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo tag-manager" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <Routes>
          <Route path='/' element={<MainView />}></Route>
          <Route path='/first' element={<FirstVIew />}></Route>
          <Route path='/second' element={<SecondVIew />}></Route>
        </Routes>
      </header>
    </div>
  );
}
