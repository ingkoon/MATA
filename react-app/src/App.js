import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
import {Route, Routes, useLocation} from "react-router-dom";
import FirstVIew from "./view/FirstVIew";
import SecondVIew from "./view/SecondView";
import MainView from "./view/MainView";
import TagManager from "./module";
import * as events from "events";

const tagManager = new TagManager('http://ec2-3-38-85-143.ap-northeast-2.compute.amazonaws.com/api/v1/dump', "tag-manager-service-token", ['click'])

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
