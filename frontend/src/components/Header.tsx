"use client"

import { usePathname } from 'next/navigation';
import Link from 'next/link';
import React, { useState } from 'react';

const Header = () => {
  const pathName = usePathname();

  const headerLinks = [
    { text: 'Home', route: '/' },
    { text: 'Edit Machine', route: '/machine' },
  ];

  const [isOpen, setIsOpen] = useState(false);
  const [isToggleInProgress, setIsToggleInProgress] = useState(false);
  
  const handleClick = () => {
    console.log('click');
    if (!isToggleInProgress) {
      setIsToggleInProgress(true);
      setIsOpen(!isOpen);
      console.log('opened');

      setTimeout(() => {
        setIsToggleInProgress(false);
      }, 300);
    }
  };

  return (
    <>
    <header className="w-full h-auto flex justify-center items-center px-[2rem] top-0 bg-[--header-background-color] z-50">
      <div className="flex justify-between max-w-[1400px] items-center w-full border-black-500 pt-[2rem] pb-[.7rem]">
        <h1 className="text-[--foreground-color] font-extrabold text-[1.5rem]">Gestão</h1>
        <nav>
          <ul className="flex items-center gap-[2rem] max-[769px]:hidden max-lg:gap-[1.5rem]">

            {headerLinks.map((link) => (
              <li key={link.route}>
                <Link
                  key={link.route}
                  href={link.route}
                  className={`text-[--foreground-color] ${(pathName === link.route ? 'font-bold' : 'font-normal')}`}
                >
                  {link.text}
                </Link>
              </li>
            ))}

          </ul>

          <button onClick={handleClick} aria-label="open" className="flex-col justify-center items-center w-[40px] h-[40px] bg-transparent hidden max-[769px]:flex relative z-[100]">
            <span className={`bg-[--foreground-color] block transition-all duration-300 ease-out 
                            h-0.5 w-6 rounded-sm ${isOpen ? 
                            'rotate-45 translate-y-1' : '-translate-y-0.5'
                            }`}>
            </span>
            <span className={`bg-[--foreground-color] block transition-all duration-300 ease-out 
                            h-0.5 w-6 rounded-sm my-0.5 ${isOpen ? 
                            'opacity-0' : 'opacity-100'
                            }`}>
            </span>
            <span className={`bg-[--foreground-color] block transition-all duration-300 ease-out 
                            h-0.5 w-6 rounded-sm ${isOpen ? 
                            '-rotate-45 -translate-y-1' : 'translate-y-0.5'
                            }`}>
            </span>
          </button>
        </nav>
      </div>
    </header>
    {
      isOpen && ( 
        <aside id="sidebar-menu" className="w-full z-[99] pointer-events-none transition-all duration-300 ease-out opacity-100 min-[769px]:hidden">
          <div className="w-[60%] h-[50%] right-[0%] -mt-8 absolute pointer-events-auto bg-[--aside-background-color] rounded-l-3xl">
            <nav className="w-full h-full">
              <ul className="flex flex-col justify-center w-full h-full p-4 gap-[5%]">
                {headerLinks.map((link) => (
                  <li key={link.route} className="flex justify-center items-center">
                    <Link
                      key={link.route}
                      href={link.route}
                      onClick={handleClick}
                      className={`text-[--foreground-color] ${(pathName === link.route ? 'font-bold' : 'font-normal')}`}
                    >
                      {link.text}
                    </Link>
                  </li>
                ))}
              </ul>
            </nav>
          </div>
        </aside>
      )
    }
    </>
  );
};

export default Header;