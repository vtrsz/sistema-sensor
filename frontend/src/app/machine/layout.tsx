import type { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Home',
  description: 'Home just for testing',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (<>
    {children}
  </>)
}