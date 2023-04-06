import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
import {Route, Routes, useLocation} from "react-router-dom";
import FirstVIew from "./view/FirstVIew";
import SecondVIew from "./view/SecondView";
import MainView from "./view/MainView";
import TagManager from "./module";

const tagManager = new TagManager("https://ec2-3-38-85-143.ap-northeast-2.compute.amazonaws.com/api/v1/dump", "c0611632-43dc-498a-917b-a884344ecfdd", ['click'], "*")


export default function App() {
  const location = useLocation()

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
