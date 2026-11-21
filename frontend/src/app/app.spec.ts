import { TestBed } from '@angular/core/testing';
import Keycloak from 'keycloak-js';
import type { Mock } from 'vitest';
import { App } from './app';

describe('App', () => {
  let keycloak: { tokenParsed: { name: string }; logout: Mock };

  beforeEach(async () => {
    keycloak = { tokenParsed: { name: 'Client Test' }, logout: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [App],
      providers: [{ provide: Keycloak, useValue: keycloak }],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should show the user name', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    expect(fixture.nativeElement.textContent).toContain('Client Test');
  });

  it('should log out through Keycloak', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    fixture.nativeElement.querySelector('button').click();

    expect(keycloak.logout).toHaveBeenCalledWith({ redirectUri: window.location.origin });
  });
});
